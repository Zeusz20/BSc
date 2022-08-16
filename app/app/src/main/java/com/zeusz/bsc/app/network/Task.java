package com.zeusz.bsc.app.network;

import android.app.Activity;

import com.zeusz.bsc.app.MainActivity;
import com.zeusz.bsc.app.dialog.InfoDialog;
import com.zeusz.bsc.app.ui.Menu;
import com.zeusz.bsc.core.Localization;

/**
 * Wraps the Runnable class in a try-catch block.
 * If an exception occurs disconnects and destroys the game client.
 * */
public final class Task implements Runnable {

    public interface ThrowingRunnable {
        void run() throws Exception;
    }

    private final Activity ctx;
    private final ThrowingRunnable target;

    public Task(Activity ctx, ThrowingRunnable target) {
        this.ctx = ctx;
        this.target = target;
    }

    @Override
    public void run() {
        try {
            target.run();
        }
        catch(Exception e) {
            MainActivity activity = (MainActivity) ctx;
            activity.destroyGameClient();
            new InfoDialog(ctx, Localization.localize("net.connection_error")).show();
            Menu.show(ctx);
        }
    }

}
