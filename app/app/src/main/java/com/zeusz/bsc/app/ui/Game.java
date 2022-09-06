package com.zeusz.bsc.app.ui;

import android.app.Activity;
import android.view.View;

import com.zeusz.bsc.app.R;
import com.zeusz.bsc.app.util.Dictionary;
import com.zeusz.bsc.app.util.IOManager;
import com.zeusz.bsc.app.widget.SendButton;
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

    /** Update game state based on incoming data from the server. */
    public void update(Activity ctx, Dictionary data) {
        // toggle send button's state
        turn = !turn;
        View content = ctx.getWindow().getDecorView().findViewById(android.R.id.content);
        ((SendButton) content.findViewById(R.id.send_button)).toggleState(ctx);

        // parse json response
        if(data.getBoolean("answer") != null) {
            // player got an answer for their question
            // TODO update question history
            DialogBuilder.showAnswer(ctx, data.getString("question"), data.getBoolean("answer"));
        }
        else if(data.getString("question") != null) {
            // player have been asked a question
            DialogBuilder.showQuestion(ctx, data.getString("attribute"), data.getString("value"), data.getString("question"));
        }
        else if(data.getString("object") != null) {
            // player got a guess
            // TODO show guess dialog
        }
    }

    public Project getProject() { return project; }

    public Object getObject() { return object; }

    public boolean getTurn() { return turn; }

    public void setObject(Object object) { this.object = object; }

    public void setTurn(boolean turn) { this.turn = turn; }


}
