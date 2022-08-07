package com.zeusz.bsc.app.network;

import android.app.Activity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;


public class Client implements Closeable, Runnable {

    /* Client states */
    public enum State { CREATE, JOIN, IN_GAME }

    /* Class fields and methods */
    protected Activity ctx;
    protected State state;  // TODO thread safety

    protected Socket socket;
    protected BufferedReader reader;
    protected BufferedWriter writer;

    public Client(Activity ctx, String host, int port, int bufferSize) throws Exception {
        this.ctx = ctx;
        this.socket = new Socket(host, port);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()), bufferSize);
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()), bufferSize);
    }

    public void listen() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            while(socket.isConnected())
                parseResponse(reader.readLine());
        }
        catch(IOException e) {
            close();
        }
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

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

}
