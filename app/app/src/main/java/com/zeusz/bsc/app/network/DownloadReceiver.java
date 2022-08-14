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

        // inform machine
        new Thread(new Task(ctx, () -> {
            if(ctx.getGameClient() != null)
                ctx.getGameClient().load(filename);
        })).start();

        // inform user
        Game.info(ctx, Localization.localize("browser.download_complete"));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        inform(context);
        context.unregisterReceiver(this);
    }



}
