package com.zeusz.bsc.app.network;

import android.app.Activity;
import android.widget.Toast;

import com.zeusz.bsc.app.MainActivity;
import com.zeusz.bsc.app.io.Dictionary;
import com.zeusz.bsc.app.io.IOManager;
import com.zeusz.bsc.app.ui.Game;
import com.zeusz.bsc.core.Cloud;
import com.zeusz.bsc.core.Localization;
import com.zeusz.bsc.core.Project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.Socket;


public class GameClient implements Closeable {

    /* Static functionality */
    public static final Dictionary SERVER_INFO = ServerInfo.getInstance().fetch();

    private static void launch(Activity ctx, Project project, State state, String action, String id) {
        new Thread(() -> {
            MainActivity activity = (MainActivity) ctx;

            try {
                GameClient client = new GameClient(ctx, project);

                activity.setGameClient(client);
                client.setState(state);
                client.connect();
                client.listen();
                client.send(action);

                if(id != null)
                    client.send(id);
            }
            catch(Exception e) {
                // failed to connect
                activity.setGameClient(null);
                Toast.makeText(ctx, Localization.localize("game.connection_error"), Toast.LENGTH_LONG).show();
            }
        }).start();
    }

    public static void createGame(Activity ctx, Project project) {
        launch(ctx, project, State.CREATE, SERVER_INFO.getString("create"), null);
    }

    public static void joinGame(Activity ctx, String id) {
        launch(ctx, null, State.JOIN, SERVER_INFO.getString("join"), id);
    }

    /* Client states */
    public enum State { CREATE, WAITING, JOIN, HANDSHAKE, IN_GAME }

    /* Class fields and methods */
    protected final Game game;
    protected String id;

    protected State state;
    protected boolean isHost;

    protected Socket socket;
    protected BufferedReader reader;
    protected BufferedWriter writer;

    public GameClient(Activity ctx, Project project) throws Exception {
        if(SERVER_INFO == null)
            throw new ConnectException("Couldn't connect to server");

        game = new Game(ctx, project);
    }

    public void setState(State state) { this.state = state; }

    public void setId(String id) { this.id = id; }

    public String getId() { return id; }

    public void connect() throws Exception {
        if(socket == null || !socket.isConnected()) {
            socket = new Socket(SERVER_INFO.getString("host"), SERVER_INFO.getInt("port"));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()), SERVER_INFO.getInt("buffer"));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()), SERVER_INFO.getInt("buffer"));
        }
    }

    public void listen() {
        new Thread(() -> {
            try {
                while(socket.isConnected())
                    parse(reader.readLine());
            }
            catch(Exception e) {
                close();
            }
        }).start();
    }

    public void send(String message) throws IOException {
        writer.write(message);
        writer.newLine();
        writer.flush();
    }

    public void parse(String response) throws Exception {
        if(response == null) return;

        switch(state) {
            case CREATE: initGame(true, response); break;
            case JOIN: initGame(false, response); break;
            case WAITING: onConnected(response); break;
            case HANDSHAKE: handshake(response); break;
            case IN_GAME: game.update(new Dictionary(response)); break;
        }
    }

    protected void initGame(boolean isHost, String id) {
        // game doesn't exist
        if(id.equals(SERVER_INFO.getString("invalid")))
            Toast.makeText(game.getContext(), Localization.localize("game.invalid"), Toast.LENGTH_SHORT).show();

        this.isHost = isHost;
        this.id = id;

        setState(State.WAITING);    // wait for other player
        if(isHost) game.waitForPlayer();
    }

    protected void onConnected(String response) throws Exception {
        if(response.equals(SERVER_INFO.getString("handshake")))
            setState(State.HANDSHAKE);

        // send which project will be played
        if(isHost) {
            send(SERVER_INFO.getString("handshake"));
            send(id);   // needed for identification
            send(game.getProject().getSource().getName());
        }
    }

    protected void handshake(String filename) {
        // download hosted game file if not present on machine
        if(!isHost) {
            String url = Cloud.getCloudUrl("/projects/" + filename);
            IOManager.download(game.getContext(), url);
            game.loadProject(filename);
        }

        setState(State.IN_GAME);
        game.start();
    }

    @Override
    public void close() {
        Thread disconnect = new Thread(() -> {
            try { send(SERVER_INFO.getString("disconnect")); }
            catch(IOException e) { /* couldn't connect to server */ }
        });

        try {
            // disconnect
            disconnect.start();
            disconnect.join();

            // close channels
            if(reader != null) reader.close();
            if(writer != null) writer.close();
            if(socket != null) socket.close();
        }
        catch(Exception e) {
            // couldn't close channels
        }
        finally {
            // destroy client instance
            MainActivity activity = (MainActivity) game.getContext();
            activity.setGameClient(null);
        }
    }

}
