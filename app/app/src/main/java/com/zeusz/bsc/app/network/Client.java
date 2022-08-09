package com.zeusz.bsc.app.network;

import android.app.Activity;

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


public class Client implements Closeable {

    /* Static functionality */
    public static final JSONObject SERVER_INFO = ServerInfo.getInstance().fetch();

    public static void launch(Activity ctx, Project project, State initial) {
        new DaemonThread(() -> {
            try {
                Client client = new Client(initial);
                // TODO start game
            }
            catch(Exception e) {
                // failed to connect
                // TODO inform user
            }
        }).start();
    }

    /* Client states */
    public enum State { CREATE, JOIN, IN_GAME }

    /* Class fields and methods */
    protected State state;  // TODO thread safety
    protected Socket socket;
    protected BufferedReader reader;
    protected BufferedWriter writer;

    public Client(State initialState) throws Exception {
        if(SERVER_INFO == null)
            throw new ConnectException("Couldn't connect to server");

        state = initialState;
        connect();
        listen();
    }

    public void connect() throws Exception {
        if(socket == null || !socket.isConnected()) {
            socket = new Socket(SERVER_INFO.getString("host"), SERVER_INFO.getInt("port"));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()), SERVER_INFO.getInt("buffer"));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()), SERVER_INFO.getInt("buffer"));
        }
    }

    public void listen() {
        new DaemonThread(() -> {
            try {
                while(socket.isConnected())
                    parseResponse(reader.readLine());
            }
            catch(IOException e) {
                close();
            }
        }).start();
    }

    public void parseResponse(String response) {
        switch(state) {
            // TODO parse based on state
        }
    }

    public void send(String message) throws IOException {
        writer.write(message);
        writer.newLine();
        writer.flush();
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

}
