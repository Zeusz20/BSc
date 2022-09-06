package com.zeusz.bsc.app.network;

import android.app.Activity;

import com.zeusz.bsc.app.MainActivity;
import com.zeusz.bsc.app.dialog.GameDialog;
import com.zeusz.bsc.app.util.Dictionary;
import com.zeusz.bsc.core.Cloud;
import com.zeusz.bsc.core.Localization;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public abstract class Channel implements Closeable {

    /* Static functionalities */
    protected static final int WAIT = 20;

    protected static final java.lang.Object INPUT_LOCK = new java.lang.Object();
    protected static final java.lang.Object OUTPUT_LOCK = new java.lang.Object();

    protected static Dictionary SERVER_INFO;

    public static boolean isAvailable(Activity ctx) {
        if(SERVER_INFO == null) Channel.fetch();

        if(SERVER_INFO == null) {
            new GameDialog(ctx, Localization.localize("net.server_unavailable")).show();
            return false;
        }

        return true;
    }

    private static void fetch() {
        try {
            // connect to server to get server info
            Thread task = new Thread(Channel::wrapJSONResponse);
            task.start();
            task.join();   // wait for task to finish
        }
        catch(InterruptedException e) { /* ignore */ }
    }

    private static void wrapJSONResponse() {
        try(InputStream stream = new URL(Cloud.getCloudUrl("/game")).openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            SERVER_INFO = new Dictionary(reader.readLine());  // read server response
        }
        catch(Exception e) {
            SERVER_INFO = null;
        }
    }


    /* Class fields and methods */
    protected final MainActivity ctx;

    protected boolean isConnected;

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    protected Channel(Activity ctx) throws ConnectException {
        if(SERVER_INFO == null)
            throw new ConnectException("Couldn't connect to server");

        this.ctx = (MainActivity) ctx;
    }

    public void connect() throws Exception {
        if(!isConnected && (socket == null || !socket.isConnected())) {
            socket = new Socket(SERVER_INFO.getString("host"), SERVER_INFO.getInt("port"));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()), SERVER_INFO.getInt("buffer"));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()), SERVER_INFO.getInt("buffer"));
            isConnected = true;
        }
    }

    public void disconnect() {
        isConnected = false;

        try {
            // disconnect from server
            send(SERVER_INFO.getString("disconnect"));
        }
        catch(Exception e) {
            // couldn't disconnect cleanly from the server
        }
        finally {
            try { close(); }
            catch(IOException e) { /* couldn't close channels */ }
        }
    }

    public void listen() {
        new Thread(new Task(ctx, () -> {
            while(socket.isConnected() && this.isConnected)
                parse(reader.readLine());
        })).start();
    }

    public void send(String message) throws Exception {
        Thread thread = new Thread(new Task(ctx, () -> {
            synchronized(Channel.OUTPUT_LOCK) {
                // wait for server to process previous sent message before client can send another one
                Thread.sleep(WAIT);

                writer.write(message);
                writer.newLine();
                writer.flush();
            }
        }));

        thread.start();
        thread.join();
    }

    public void handshake(String action, String initialMsg) throws Exception {
        send(action);
        send(initialMsg);
    }

    public abstract void parse(String response) throws Exception;

    @Override
    public void close() throws IOException {
        if(reader != null) reader.close();
        if(writer != null) writer.close();
        if(socket != null) socket.close();
    }

}
