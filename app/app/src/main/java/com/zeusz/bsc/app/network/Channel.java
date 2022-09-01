package com.zeusz.bsc.app.network;

import android.app.Activity;

import com.zeusz.bsc.app.MainActivity;
import com.zeusz.bsc.app.ui.Dialog;
import com.zeusz.bsc.app.ui.Menu;
import com.zeusz.bsc.app.util.Dictionary;
import com.zeusz.bsc.core.Cloud;

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

    /* Static functionality */
    protected static final java.lang.Object LOCK = new java.lang.Object();

    protected static Dictionary SERVER_INFO;

    public static boolean isAvailable(Activity ctx) {
        if(SERVER_INFO == null) Channel.fetch();

        if(SERVER_INFO == null) {
            Dialog.error(ctx, "net.server_unavailable");
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

    private boolean isConnected;
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

    public void listen() {
        new Thread(new Task(ctx, () -> {
            while(socket.isConnected() && this.isConnected)
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

    public void handshake(String action, String initialMsg) throws Exception {
        send(action);
        send(initialMsg);
    }

    public abstract void parse(String response) throws Exception;

    @Override
    public void close() {
        isConnected = false;

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
