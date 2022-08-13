package com.zeusz.bsc.app.ui;

import android.app.Activity;

import com.zeusz.bsc.app.R;
import com.zeusz.bsc.app.io.Dictionary;
import com.zeusz.bsc.app.io.IOManager;
import com.zeusz.bsc.core.Project;


public final class Game {

    private final Activity ctx;
    private Project project;

    public Game(Activity ctx, Project project) {
        this.ctx = ctx;
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

    public Activity getContext() { return ctx; }

    public Project getProject() { return project; }

}
