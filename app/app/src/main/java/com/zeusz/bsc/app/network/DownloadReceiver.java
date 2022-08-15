package com.zeusz.bsc.app.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zeusz.bsc.app.MainActivity;
import com.zeusz.bsc.app.ui.Game;
import com.zeusz.bsc.core.Localization;


public class DownloadReceiver extends BroadcastReceiver {

    protected String filename;

    public DownloadReceiver(String filename) {
        this.filename = filename;
    }

    public void inform(Context context) {
        MainActivity ctx = (MainActivity) context;

        /* INFORM MACHINE
         * Client runs on the networking thread,
         * therefore, the game cannot be loaded on the main thread. */
        new Thread(new Task(ctx, () -> {
            if(ctx.getGameClient() != null)
                ctx.getGameClient().load(filename);
        })).start();

        /* INFORM USER
         * If game client isn't running (null) show toast message,
         * because download was initiated from the JS browser. */
        if(ctx.getGameClient() == null)
            Game.info(ctx, Localization.localize("browser.download_complete"));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        inform(context);
        context.unregisterReceiver(this);
    }

}
