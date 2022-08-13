package com.zeusz.bsc.app.network;

import android.app.Activity;

import com.zeusz.bsc.app.MainActivity;
import com.zeusz.bsc.app.ui.Game;
import com.zeusz.bsc.core.Localization;

/** Wraps the Runnable class in a try-catch block. */
public final class Task implements Runnable {

    public interface ThrowingRunnable {
        void run() throws Exception;
    }

    private Activity ctx;
    private ThrowingRunnable target;

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

            Game.info(activity, Localization.localize("game.connection_error"));
        }
    }

}
