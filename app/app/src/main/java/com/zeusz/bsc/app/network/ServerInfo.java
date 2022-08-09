package com.zeusz.bsc.app.network;

import com.zeusz.bsc.core.Cloud;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;


final class ServerInfo implements Runnable {

    /* Singleton */
    private static final ServerInfo INSTANCE = new ServerInfo();
    public static ServerInfo getInstance() { return INSTANCE; }

    /* Class fields and methods */
    private JSONObject[] wrapper;

    private ServerInfo() {
        wrapper = new JSONObject[]{ null };

        try {
            // connect to server to get server info
            Thread fetch = new Thread(this);
            fetch.start();
            fetch.join();   // wait for task to finish
        }
        catch(InterruptedException e) { /* ignore */ }
    }

    public JSONObject fetch() {
        return wrapper[0];
    }

    @Override
    public void run() {
        try(InputStream stream = new URL(Cloud.getCloudUrl("/game")).openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {

            // read server response
            wrapper[0] = new JSONObject(reader.readLine());
        }
        catch(Exception e) {
            wrapper[0] = null;
        }
    }

}
