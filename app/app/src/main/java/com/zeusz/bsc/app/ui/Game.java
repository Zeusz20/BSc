package com.zeusz.bsc.app.ui;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.zeusz.bsc.app.R;
import com.zeusz.bsc.app.io.Dictionary;
import com.zeusz.bsc.app.io.IOManager;
import com.zeusz.bsc.core.Project;


public final class Game {

    /* Static functionality */
    public static void info(Context context, String message) {
        ((Activity) context).runOnUiThread(() -> {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        });
    }

    /* Class fields and methods */
    private Project project;

    public Game(Project project) {
        this.project = project;
    }

    public void loadingScreen(Activity ctx) {
        ctx.runOnUiThread(() -> Menu.show(ctx, Menu.WAITING_FOR_PLAYER));
    }

    public void start(Activity ctx) {
        ctx.runOnUiThread(() -> ctx.setContentView(R.layout.game_layout));
    }

    /** Update game state based on incoming data from the server. */
    public void update(Activity ctx, Dictionary data) {

    }

    public void loadProject(Activity ctx, String filename) {
        project = IOManager.loadProjectByFilename(ctx, filename);
    }

    public Project getProject() { return project; }

}
