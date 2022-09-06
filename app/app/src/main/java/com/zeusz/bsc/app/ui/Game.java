package com.zeusz.bsc.app.ui;

import android.app.Activity;

import com.zeusz.bsc.app.MainActivity;
import com.zeusz.bsc.app.R;
import com.zeusz.bsc.app.dialog.GameDialog;
import com.zeusz.bsc.app.dialog.GuessDialog;
import com.zeusz.bsc.app.dialog.QuestionDialog;
import com.zeusz.bsc.app.util.Dictionary;
import com.zeusz.bsc.app.util.IOManager;
import com.zeusz.bsc.app.widget.SendButton;
import com.zeusz.bsc.core.Attribute;
import com.zeusz.bsc.core.Item;
import com.zeusz.bsc.core.Localization;
import com.zeusz.bsc.core.Object;
import com.zeusz.bsc.core.Project;

import java.util.List;


public final class Game {

    /* Static functionalities */
    public static <T extends Item> T findItemByName(MainActivity ctx, String name, Class<T> type) {
        List<T> items = ctx.getGameClient().getGame().getProject().getItemList(type);
        return items.stream().filter(it -> it.getName().equals(name)).findAny().get();
    }

    public static boolean objectHasAttribute(MainActivity ctx, Attribute attribute, String value) {
        Object object = ctx.getGameClient().getGame().getObject();
        return object.getAttributes().stream().anyMatch(it -> it.getKey().equals(attribute) && it.getValue().equals(value));
    }

    /* Class fields and methods */
    private Project project;
    private Object object;
    private boolean win;

    public Game(Project project) {
        this.project = project;
    }

    public void loadProject(Activity ctx, String filename) {
        project = IOManager.loadProjectByFilename(ctx, filename);
    }

    public void exit(Activity ctx, boolean isDirty) {
        ViewManager.show(ctx, ViewManager.MAIN_MENU);

        if(isDirty)
            new GameDialog(ctx, Localization.localize("game.player_disconnected")).show();
    }

    /**
     * Update game state based on incoming data from the server.
     *
     * + Shows the action's corresponding dialog:
     *  1) get an answer
     *  2) ask a question
     *  3) guess
     */
    public void update(Activity ctx, Dictionary data) {
        // parse json response
        if(data.getBoolean("answer") != null) {
            // player got an answer for their question
            boolean answer = data.getBoolean("answer");
            int icon = answer ? R.drawable.positive_feedback : R.drawable.negative_feedback;
            String title = Localization.localize(answer ? "word.yes" : "word.no");

            new GameDialog(ctx, icon, title, data.getString("question")).show();
        }
        else if(data.getString("question") != null) {
            // player have been asked a question
            new QuestionDialog(ctx, data.getString("attribute"), data.getString("value"), data.getString("question")).show();
            // TODO update question history
            SendButton.toggleAll(ctx);
        }
        else if(data.getString("object") != null) {
            // opponent guessed player's object
            new GuessDialog(ctx, data.getString("object")).show();
            SendButton.toggleAll(ctx);
        }

        // TODO end game and display winner/loser
    }

    public Project getProject() { return project; }

    public Object getObject() { return object; }

    public void setObject(Object object) { this.object = object; }

}
