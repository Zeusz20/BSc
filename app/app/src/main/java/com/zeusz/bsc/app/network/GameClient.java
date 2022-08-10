package com.zeusz.bsc.app.network;

import android.app.Activity;

import com.zeusz.bsc.app.IOManager;
import com.zeusz.bsc.core.Cloud;
import com.zeusz.bsc.core.Project;

import org.json.JSONObject;

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
    public static final JSONObject SERVER_INFO = ServerInfo.getInstance().fetch();

    public static void launch(Activity ctx, Project project, State initial) {
        new Thread(() -> {
            try {
                if(initial != State.CREATE && initial != State.JOIN)
                    throw new ConnectException("Initial state cannot be " + initial.name());

                GameClient client = new GameClient(ctx, project);
                client.setState(initial);
                client.connect();
                client.listen();

                String message = (initial == State.CREATE) ? "create" : "join";
                client.send(SERVER_INFO.getString(message));
            }
            catch(Exception e) {
                // failed to connect
                // TODO inform user
                System.out.println(e.getMessage());
            }
        }).start();
    }

    /* Client states */
    public enum State { CREATE, WAITING, JOIN, HANDSHAKE, IN_GAME }

    /* Class fields and methods */
    protected Activity ctx;
    protected Project project;

    protected State state;
    protected String gameID;
    protected boolean isHost;

    protected Socket socket;
    protected BufferedReader reader;
    protected BufferedWriter writer;

    public GameClient(Activity ctx, Project project) throws Exception {
        if(SERVER_INFO == null)
            throw new ConnectException("Couldn't connect to server");

        this.ctx = ctx;
        this.project = project;
    }

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
            case CREATE: initGame(response, true); break;
            case JOIN: initGame(response, false); break;
            case WAITING: onConnected(response); break;
            case HANDSHAKE: handshake(response); break;

            case IN_GAME:
                JSONObject jsonResponse = new JSONObject(response);
                break;
        }
    }

    protected void initGame(String gameID, boolean isHost) {
        this.gameID = gameID;
        this.isHost = isHost;

        setState(State.WAITING);    // wait for other player
    }

    protected void handshake(String filename) {
        // download hosted game file if not present on machine
        if(!isHost) {
            String url = Cloud.getCloudUrl("/projects/" + filename);
            IOManager.download(ctx, url);

            project = IOManager.loadProjectByFilename(ctx, filename);
        }

        setState(State.IN_GAME);
    }

    protected void onConnected(String response) throws Exception {
        if(response.equals(SERVER_INFO.getString("handshake")))
            setState(State.HANDSHAKE);

        // send which project will be played
        if(isHost) {
            send(gameID);   // needed for identification
            send(SERVER_INFO.getString("handshake"));
            send(project.getSource().getName());
        }
    }

    @Override
    public void close() {
        try {
            if(reader != null) reader.close();
            if(writer != null) writer.close();
            if(socket != null) socket.close();
        }
        catch(IOException e) {
            // couldn't close channels
        }
    }

    public void setState(State state) {
        this.state = state;
    }

}
