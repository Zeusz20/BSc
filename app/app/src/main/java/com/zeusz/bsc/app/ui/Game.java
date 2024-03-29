package com.zeusz.bsc.app.ui;

import android.app.Activity;

import androidx.annotation.Nullable;

import com.zeusz.bsc.app.MainActivity;
import com.zeusz.bsc.app.R;
import com.zeusz.bsc.app.dialog.AnswerDialog;
import com.zeusz.bsc.app.dialog.GameDialog;
import com.zeusz.bsc.app.dialog.GuessDialog;
import com.zeusz.bsc.app.dialog.QuestionDialog;
import com.zeusz.bsc.app.layout.HistoryLayout;
import com.zeusz.bsc.app.util.Dictionary;
import com.zeusz.bsc.app.util.IOManager;
import com.zeusz.bsc.app.widget.SendButton;
import com.zeusz.bsc.core.Attribute;
import com.zeusz.bsc.core.Item;
import com.zeusz.bsc.core.Localization;
import com.zeusz.bsc.core.Object;
import com.zeusz.bsc.core.Pair;
import com.zeusz.bsc.core.Project;

import java.util.List;
import java.util.Optional;


public final class Game {

    /* Static functionalities */
    public static <T extends Item> T findItemByName(MainActivity ctx, String name, Class<T> type) {
        List<T> items = ctx.getGameClient().getGame().getProject().getItemList(type);
        return items.stream().filter(it -> it.getName().equals(name)).findAny().get();
    }

    public static Boolean objectHasAttribute(MainActivity ctx, Attribute attribute, String value) {
        Object object = ctx.getGameClient().getGame().getObject();

        // check if attribute is present
        Optional<Pair<Attribute, String>> attrValue = object.getAttributes().stream()
                .filter(it -> it.getKey().equals(attribute))
                .findAny();

        if(attrValue.isPresent()) {
            // object has the attribute but not necessarily equals to the given value
            return object.getAttributes().stream().anyMatch(it -> it.getKey().equals(attribute) && it.getValue().equals(value));
        }
        else {
            // object does not have the attribute
            return null;
        }
    }

    /* Class fields and methods */
    private Project project;
    private Object object;

    public Game(Activity ctx, @Nullable Project project) {
        if(project != null)
            loadProject(ctx, project.getSource().getName());
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
        if(data.hasKey("answer")) { // answer CAN BE null
            // player got an answer for their question/guess
            Boolean answer = data.getBoolean("answer");
            HistoryLayout history = ctx.findViewById(R.id.history_layout);

            if(data.getString("object") != null) {
                // answer to guess
                Object object = findItemByName((MainActivity) ctx, data.getString("object"), Object.class);
                new AnswerDialog(ctx, object, answer).show();
                history.add(ctx, object.getName(), answer);
            }
            else {
                // answer to question
                new AnswerDialog(ctx, data.getString("question"), answer).show();
                history.add(ctx, data.getString("question"), answer);
            }
        }
        else if(data.getString("question") != null) {
            // player have been asked a question
            Attribute attribute = Game.findItemByName((MainActivity) ctx, data.getString("attribute"), Attribute.class);

            new QuestionDialog(ctx, attribute, data.getString("value"), data.getString("question")).show();
            SendButton.toggleAll(ctx);
        }
        else if(data.getString("object") != null) {
            // opponent guessed player's object
            Object object = findItemByName((MainActivity) ctx, data.getString("object"), Object.class);

            new GuessDialog(ctx, object).show();

            // if opponent guessed correctly, don't let player to send or guess anymore
            if(!getObject().equals(object)) SendButton.toggleAll(ctx);
        }
    }

    public Project getProject() { return project; }

    public Object getObject() { return object; }

    public void setObject(Object object) { this.object = object; }

}
