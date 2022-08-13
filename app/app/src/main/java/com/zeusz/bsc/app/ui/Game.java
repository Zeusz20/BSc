package com.zeusz.bsc.app.ui;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.zeusz.bsc.app.MainActivity;
import com.zeusz.bsc.app.R;
import com.zeusz.bsc.app.io.Dictionary;
import com.zeusz.bsc.app.io.IOManager;
import com.zeusz.bsc.core.Project;


public final class Game {

    /* Static functionality */
    public static void info(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /* Class fields and methods */
    private final MainActivity ctx;
    private Project project;

    public Game(Activity ctx, Project project) {
        this.ctx = (MainActivity) ctx;
        this.project = project;
    }

    public void waitForPlayer() {
        ctx.runOnUiThread(() -> Menu.show(ctx, Menu.WAITING_FOR_PLAYER));
    }

    public void start() {
        ctx.runOnUiThread(() -> ctx.setContentView(R.layout.game_layout));
    }

    /** Update game state based on incoming data from the server. */
    public void update(Dictionary data) {

    }

    public void loadProject(String filename) {
        project = IOManager.loadProjectByFilename(ctx, filename);
    }

    public MainActivity getContext() { return ctx; }

    public Project getProject() { return project; }

}
