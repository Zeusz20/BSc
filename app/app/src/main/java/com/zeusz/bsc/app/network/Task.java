package com.zeusz.bsc.app.network;

import android.app.Activity;

import com.zeusz.bsc.app.MainActivity;
import com.zeusz.bsc.app.ui.DialogBuilder;
import com.zeusz.bsc.app.ui.ViewManager;

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
            DialogBuilder.error(ctx, "net.connection_error");
            ViewManager.show(ctx, ViewManager.MAIN_MENU);
        }
    }

}
