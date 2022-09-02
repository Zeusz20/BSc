package com.zeusz.bsc.app.ui;

import android.app.Activity;

import com.zeusz.bsc.app.R;
import com.zeusz.bsc.app.util.Dictionary;
import com.zeusz.bsc.app.util.IOManager;
import com.zeusz.bsc.core.Project;


public final class Game {

    private Project project;

    public Game(Project project) {
        this.project = project;
    }

    public void loadingScreen(Activity ctx) {
        ctx.runOnUiThread(() -> ViewManager.show(ctx, ViewManager.LOADING_SCREEN));
    }

    public void start(Activity ctx) {
        ctx.runOnUiThread(() -> ctx.setContentView(R.layout.game_layout));
    }

    public void exit(Activity ctx, boolean isDirty) {
        ctx.runOnUiThread(() -> ViewManager.show(ctx, ViewManager.MAIN_MENU));
        if(isDirty) Dialog.error(ctx, "game.player_disconnected");
    }

    /** Update game state based on incoming data from the server. */
    public void update(Activity ctx, Dictionary data) {

    }

    public void loadProject(Activity ctx, String filename) {
        project = IOManager.loadProjectByFilename(ctx, filename);
    }

    public Project getProject() { return project; }

}
