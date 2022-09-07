package com.zeusz.bsc.app.dialog;

import android.app.Activity;

import com.zeusz.bsc.app.MainActivity;
import com.zeusz.bsc.app.ui.Game;
import com.zeusz.bsc.app.util.Dictionary;
import com.zeusz.bsc.core.Attribute;
import com.zeusz.bsc.core.Localization;


public class QuestionDialog extends GameDialog {

    public QuestionDialog(Activity ctx, Attribute attribute, String value, String question) {
        super(ctx, question);

        boolean answer = Game.objectHasAttribute((MainActivity) ctx, attribute, value);

        setCancelable(false);
        setPositiveButton(Localization.localize(answer ? "word.yes" : "word.no"), (dialog, which) -> {
            ((MainActivity) ctx).getGameClient().sendJSON(new Dictionary()
                .put("attribute", attribute.getName())
                .put("value", value)
                .put("question", question)
                .put("answer", answer)
            );
        });
    }

}
