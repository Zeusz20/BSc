package com.zeusz.bsc.app.network;


import android.app.Activity;

import com.zeusz.bsc.core.Cloud;
import com.zeusz.bsc.core.Project;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public final class Game {

    /* Static functionality */
    public static JSONObject getServerInfo() {
        try(InputStream stream = new URL(Cloud.getCloudUrl("/game")).openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {

            return new JSONObject(reader.readLine());
        }
        catch(Exception e) {
            return null;
        }
    }

    /* Class fields and methods */
    private final JSONObject SERVER_INFO;

    private Activity ctx;
    private Client client;
    private Project project;

    public Game(Activity ctx, Project project) {
        SERVER_INFO = getServerInfo();

        if(SERVER_INFO == null)
            throw new NullPointerException("Couldn't connect to server");

        this.ctx = ctx;
        this.project = project;
    }

    public void launch() {
        try {
            String host = SERVER_INFO.getString("host");
            int port = SERVER_INFO.getInt("port");
            int bufferSize = SERVER_INFO.getInt("buffer");

            client = new Client(ctx, host, port, bufferSize);
            client.listen();
            client.setState(Client.State.CREATE);
            client.send(SERVER_INFO.getString("create"));
        }
        catch(Exception e) {
            // couldn't launch game
        }
    }

}
