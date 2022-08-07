package com.zeusz.bsc.app.network;

import com.zeusz.bsc.core.Cloud;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public final class NetworkManager {

    private NetworkManager() { }

    public static JSONObject getServerInfo() {
        try {
            try(InputStream stream = new URL(Cloud.getCloudUrl("/game")).openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                StringBuilder content = new StringBuilder();

                int ch;
                while((ch = reader.read()) != -1)
                    content.append((char) ch);

                return new JSONObject(content.toString());
            }
        }
        catch(Exception e) {
            // couldn't get server info
            return null;
        }
    }

}
