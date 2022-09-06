package com.zeusz.bsc.app.network;

import android.app.Activity;

import com.zeusz.bsc.app.MainActivity;
import com.zeusz.bsc.app.dialog.GameDialog;
import com.zeusz.bsc.app.ui.ViewManager;
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
            e.printStackTrace();
            ((MainActivity) ctx).setGameClient(null);
            new GameDialog(ctx, Localization.localize("net.connection_error")).show();
            ViewManager.show(ctx, ViewManager.MAIN_MENU);
        }
    }

}
