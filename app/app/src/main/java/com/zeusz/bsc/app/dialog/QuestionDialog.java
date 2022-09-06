package com.zeusz.bsc.app.dialog;

import android.app.Activity;

import com.zeusz.bsc.app.MainActivity;
import com.zeusz.bsc.app.ui.Game;
import com.zeusz.bsc.core.Attribute;
import com.zeusz.bsc.core.Localization;


public class QuestionDialog extends GameDialog {

    public QuestionDialog(Activity ctx, String attributeName, String value, String question) {
        super(ctx, question);

        Attribute attribute = Game.findItemByName((MainActivity) ctx, attributeName, Attribute.class);
        boolean answer = Game.objectHasAttribute((MainActivity) ctx, attribute, value);

        setCancelable(false);
        setPositiveButton(Localization.localize(answer ? "word.yes" : "word.no"), (dialog, which) -> {
            ((MainActivity) ctx).getGameClient().sendAnswer(attribute, value, question, answer);
        });
    }

}
