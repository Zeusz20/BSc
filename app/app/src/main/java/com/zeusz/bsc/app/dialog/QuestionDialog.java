package com.zeusz.bsc.app.dialog;

import android.app.Activity;

import com.zeusz.bsc.app.MainActivity;
import com.zeusz.bsc.core.Attribute;
import com.zeusz.bsc.core.Localization;


public class QuestionDialog extends GameDialog {

    protected QuestionDialog(Activity ctx) {
        super(ctx);
        setCancelable(false);
    }

    public QuestionDialog(Activity ctx, String attributeName, String value, String question) {
        super(ctx, question);

        Attribute attribute = findAttributeByName(ctx, attributeName);
        boolean answer = objectHasAttribute(ctx, attribute, value);

        setPositiveButton(Localization.localize(answer ? "word.yes" : "word.no"), (dialog, which) -> {
            ((MainActivity) ctx).getGameClient().sendAnswer(attribute, value, question, answer);
        });
    }

    private Attribute findAttributeByName(Activity ctx, String name) {
        return ((MainActivity) ctx).getGameClient().getGame().getProject().getItemList(Attribute.class).stream()
                .filter(it -> it.getName().equals(name))
                .findAny().get();
    }

    /** @return Selected object has the given attribute with the chosen value. */
    private boolean objectHasAttribute(Activity ctx, Attribute attribute, String value) {
        return ((MainActivity) ctx).getGameClient().getGame().getObject().getAttributes().stream()
                .anyMatch(it -> it.getKey().equals(attribute) && it.getValue().equals(value));
    }

}
