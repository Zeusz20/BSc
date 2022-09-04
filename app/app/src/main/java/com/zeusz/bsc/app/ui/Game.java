package com.zeusz.bsc.app.ui;

import android.app.Activity;

import com.zeusz.bsc.app.util.Dictionary;
import com.zeusz.bsc.app.util.IOManager;
import com.zeusz.bsc.core.Object;
import com.zeusz.bsc.core.Project;


public final class Game {

    private Project project;
    private Object object;

    public Game(Project project) {
        this.project = project;
    }

    public void loadProject(Activity ctx, String filename) {
        project = IOManager.loadProjectByFilename(ctx, filename);
    }

    public void show(Activity ctx, int screenId) {
        ctx.runOnUiThread(() -> ViewManager.show(ctx, screenId));
    }

    public void exit(Activity ctx, boolean isDirty) {
        ctx.runOnUiThread(() -> {
            ViewManager.show(ctx, ViewManager.MAIN_MENU);
            if(isDirty) DialogBuilder.error(ctx, "game.player_disconnected");
        });
    }

    /** Update game state based on incoming data from the server. */
    public void update(Activity ctx, Dictionary data) {

    }

    public Project getProject() { return project; }

    public Object getObject() { return object; }

    public void setObject(Object object) { this.object = object; }

}
