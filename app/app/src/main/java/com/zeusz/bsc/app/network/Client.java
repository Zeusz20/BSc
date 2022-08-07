package com.zeusz.bsc.app.network;

import android.app.Activity;
import android.widget.TextView;

import com.zeusz.bsc.app.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;


public class Client implements Closeable {

    protected final JSONObject SERVER_INFO;

    protected Activity ctx;
    protected Socket socket;
    protected BufferedReader reader;
    protected BufferedWriter writer;

    public Client(Activity ctx) throws Exception {
        this.ctx = ctx;
        SERVER_INFO = NetworkManager.getServerInfo();

        if(SERVER_INFO == null)
            throw new NullPointerException("Couldn't get server info");

        socket = new Socket(SERVER_INFO.getString("host"), SERVER_INFO.getInt("port"));
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()), SERVER_INFO.getInt("buffer"));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()), SERVER_INFO.getInt("buffer"));

        socket.connect(new InetSocketAddress(SERVER_INFO.getString("host"), SERVER_INFO.getInt("port")));
    }

    public void create() {
        try {
            writer.write(SERVER_INFO.getString("create"));
            writer.flush();

            StringBuilder response = new StringBuilder();
            int ch;
            while((ch = reader.read()) != -1)
                response.append((char) ch);

            ((TextView) ctx.findViewById(R.id.text_view)).setText(response.toString());
        }
        catch(Exception e) {
            // couldn't send message to server
        }
    }

    public void join(String gameID) {
        try {
            // TODO
        }
        catch(Exception e) {
            // couldn't send message to server
        }
    }

    public void send(String data) {
        try {
            // TODO
        }
        catch(Exception e) {
            // couldn't send message to server
        }
    }

    public void disconnect() {
        try {
            // TODO
        }
        catch(Exception e) {
            // couldn't send message to server
        }
    }

    @Override
    public void close() throws IOException {
        if(reader != null) reader.close();
        if(writer != null) writer.close();
        if(socket != null) socket.close();
    }

}
