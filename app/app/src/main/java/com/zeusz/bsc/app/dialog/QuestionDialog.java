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

        Boolean answer = Game.objectHasAttribute((MainActivity) ctx, attribute, value);
        String caption = (answer == null) ? "word.maybe" : answer ? "word.yes" : "word.no";

        setCancelable(false);
        setPositiveButton(Localization.localize(caption), (dialog, which) -> {
            ((MainActivity) ctx).getGameClient().sendJSON(new Dictionary()
                .put("attribute", attribute.getName())
                .put("value", value)
                .put("question", question)
                .put("answer", answer)
            );
        });
    }

}
