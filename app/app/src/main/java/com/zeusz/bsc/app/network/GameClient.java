package com.zeusz.bsc.app.network;

import android.app.Activity;

import com.zeusz.bsc.app.MainActivity;
import com.zeusz.bsc.app.ui.Dialog;
import com.zeusz.bsc.app.ui.Game;
import com.zeusz.bsc.app.util.Dictionary;
import com.zeusz.bsc.app.util.IOManager;
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
import java.net.URLEncoder;


public class GameClient implements Closeable {

    /* Static functionality */
    public static Dictionary SERVER_INFO;

    /**
     * @param state
     *  In which state the client will be after creation.
     * @param action
     *  Which action will the client perform (host or join).
     * @param initial
     *  The initial message to the server. This holds the name of the projects
     *  in case of hosting a game; and the ID of the game in case of joining to one.
     * */
    private static void launch(Activity ctx, Project project, State state, String action, String initial) {
        new Thread(new Task(ctx, () -> {
            MainActivity activity = (MainActivity) ctx;
            GameClient client = new GameClient(ctx, project);

            activity.setGameClient(client);
            client.setState(state);
            client.connect();       // connect to server
            client.listen();        // wait for response from server
            client.send(action);    // send intent (create/join game)
            client.send(initial);
        })).start();
    }

    public static void createGame(Activity ctx, Project project) {
        if(ServerInfo.isAvailable(ctx)) {
            SERVER_INFO = ServerInfo.getInstance().info();
            launch(ctx, project, State.CREATE, SERVER_INFO.getString("create"), project.getSource().getName());
        }
    }

    public static void joinGame(Activity ctx, String id) {
        if(ServerInfo.isAvailable(ctx)) {
            SERVER_INFO = ServerInfo.getInstance().info();
            launch(ctx, null, State.JOIN, SERVER_INFO.getString("join"), id);
        }
    }

    /* Client states */
    public enum State { CREATE, WAITING, JOIN, FILE_DOWNLOAD, IN_GAME }

    /* Class fields and methods */
    private final MainActivity ctx;
    protected Game game;

    protected String id;
    protected State state;
    protected boolean isHost;

    protected Socket socket;
    protected BufferedReader reader;
    protected BufferedWriter writer;

    public GameClient(Activity ctx, Project project) throws Exception {
        if(SERVER_INFO == null)
            throw new ConnectException("Couldn't connect to server");

        this.ctx = (MainActivity) ctx;
        this.game = new Game(project);
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
        new Thread(new Task(ctx, () -> {
            while(socket.isConnected())
                parse(reader.readLine());
        })).start();
    }

    public void send(String message) throws Exception {
        writer.write(message);
        writer.newLine();
        writer.flush();

        // wait for server to process sent message before client can send another one
        Thread.sleep(10);
    }

    public synchronized void parse(String response) throws Exception {
        if(response == null) return;

        if(SERVER_INFO.getString("disconnect").equals(response)) {
            ctx.destroyGameClient();
            return;
        }

        switch(state) {
            case CREATE:
            case JOIN:
                // the activity which creates the game is the host
                initGame((state == State.CREATE), response);
                break;

            case WAITING:
                waitForPlayer(response);
                break;

            case FILE_DOWNLOAD:
                downloadProject(response);
                break;

            case IN_GAME:
                game.update(ctx, new Dictionary(response));
                break;
        }
    }

    /** @return The base structure of a request. */
    protected Dictionary getMessage() {
        Dictionary dictionary = new Dictionary();
        dictionary.put("is_host", isHost);
        dictionary.put("game_id", id);

        return dictionary;
    }

    /**
     * Initializes the game and the client when it is in the state of CREATE or JOIN.
     * After initialization the host is put to the WAITING state, while the
     * joining player is put in the FILE_DOWNLOAD state.
     * */
    protected void initGame(boolean isHost, String id) {
        if(id.equals(SERVER_INFO.getString("invalid"))) {
            // game doesn't exist
            Dialog.toast(ctx, Localization.localize("game.invalid"));
            ctx.destroyGameClient();
        }
        else {
            this.isHost = isHost;
            this.id = id;

            setState(isHost ? State.WAITING : State.FILE_DOWNLOAD);
            if(isHost) game.loadingScreen(ctx);  // render waiting screen
        }
    }

    /**
     * While client is in the WAITING state, it waits for the other player to
     * download the project file (if not present on their machine).
     * The connection is established when the server sends the "ready" response.
     * */
    protected void waitForPlayer(String response) throws Exception {
        if(SERVER_INFO.getString("ready").equals(response))
            load(null);  // start game
    }

    /**
     * In the FILE_DOWNLOAD state the joining player downloads the project
     * file to their machine if not present. After the download finished the
     * {@link #load(String)} method will load the project and the client's
     * state will be changed to IN_GAME.
     * */
    protected void downloadProject(String filename) throws Exception {
        if(!isHost) {
            String encoded = URLEncoder.encode(filename, SERVER_INFO.getString("format"));
            String url = Cloud.getCloudUrl("/projects/" + encoded);
            IOManager.download(ctx, url);  // download project file if missing
        }
    }

    /**
     * Loads the project to memory.
     * If joining player doesn't have the project file, this method is invoked
     * by the {@link DownloadReceiver} class when the project is finished downloading.
     * */
    public void load(String filename) throws Exception {
        // host already has the project loaded
        if(!isHost) {
            game.loadProject(ctx, filename);
            send(SERVER_INFO.getString("ready"));
            send(id);   // needed for identification
        }

        setState(State.IN_GAME);
        game.start(ctx);
    }

    @Override
    public void close() {
        Thread disconnect = new Thread(() -> {
            try { send(SERVER_INFO.getString("disconnect")); }
            catch(Exception e) { /* couldn't connect to server */ }
        });

        try {
            // disconnect from server
            disconnect.start();
            disconnect.join();
        }
        catch(Exception e) {
            // couldn't disconnect cleanly from the server
        }
        finally {
            try {
                // close channels
                if(reader != null) reader.close();
                if(writer != null) writer.close();
                if(socket != null) socket.close();
            }
            catch(IOException e) {
                // couldn't close channels
            }
        }
    }

}
