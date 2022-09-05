package com.zeusz.bsc.app.ui;

import android.app.Activity;
import android.widget.Spinner;
import android.widget.TextView;

import com.zeusz.bsc.app.R;
import com.zeusz.bsc.app.util.Dictionary;
import com.zeusz.bsc.app.util.IOManager;
import com.zeusz.bsc.core.Attribute;
import com.zeusz.bsc.core.Object;
import com.zeusz.bsc.core.Project;


public final class Game {

    private boolean turn; // is the player's turn?
    private Project project;
    private Object object;

    public Game(Project project) {
        this.project = project;
    }

    public void loadProject(Activity ctx, String filename) {
        project = IOManager.loadProjectByFilename(ctx, filename);

    }

    public void exit(Activity ctx, boolean isDirty) {
        ViewManager.show(ctx, ViewManager.MAIN_MENU);

        if(isDirty)
            DialogBuilder.error(ctx, "game.player_disconnected");
    }

    public void selectQuestion(Attribute attribute) {

    }

    public String getQuestion(Activity ctx) {
        String question1 = ((TextView) ctx.findViewById(R.id.question_part_1)).getText().toString();
        String question2 = ((TextView) ctx.findViewById(R.id.question_part_2)).getText().toString();

        return question1 + question2;
    }

    /** Update game state based on incoming data from the server. */
    public void update(Activity ctx, Dictionary data) {

    }

    public Project getProject() { return project; }

    public Object getObject() { return object; }

    public boolean getTurn() { return turn; }

    public void setObject(Object object) { this.object = object; }

    public void setTurn(boolean turn) { this.turn = turn; }


}
