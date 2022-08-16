package com.zeusz.bsc.app.network;

import android.app.Activity;

import com.zeusz.bsc.app.dialog.InfoDialog;
import com.zeusz.bsc.app.io.Dictionary;
import com.zeusz.bsc.core.Cloud;
import com.zeusz.bsc.core.Localization;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public final class ServerInfo {

    /* Static functionality */
    public static boolean isAvailable(Activity ctx) {
        getInstance().fetch();

        if(getInstance().info() == null) {
            ctx.runOnUiThread(() -> {
                new InfoDialog(ctx, Localization.localize("net.server_unavailable")).show();
            });
            return false;
        }

        return true;
    }

    /* Singleton */
    private static final ServerInfo INSTANCE = new ServerInfo();
    public static ServerInfo getInstance() { return INSTANCE; }

    /* Class fields and methods */
    private final Dictionary[] wrapper;

    private ServerInfo() {
        wrapper = new Dictionary[]{ null };
    }

    private void wrapJSONResponse() {
        try(InputStream stream = new URL(Cloud.getCloudUrl("/game")).openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {

            // read server response
            wrapper[0] = new Dictionary(reader.readLine());
        }
        catch(Exception e) {
            wrapper[0] = null;
        }
    }

    private void fetch() {
        try {
            // connect to server to get server info
            Thread task = new Thread(() -> wrapJSONResponse()); // method reference doesn't work
            task.start();
            task.join();   // wait for task to finish
        }
        catch(InterruptedException e) { /* ignore */ }
    }

    public Dictionary info() {
        return wrapper[0];
    }

}
