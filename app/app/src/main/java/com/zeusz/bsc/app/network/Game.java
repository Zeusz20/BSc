package com.zeusz.bsc.app.network;

import android.app.Activity;

import com.zeusz.bsc.core.Project;


// TODO
public final class Game {

    private Project project;

    public Game(Activity ctx, Project project) {
        this.project = project;
    }

    public Project getProject() {
        return project;
    }

}
