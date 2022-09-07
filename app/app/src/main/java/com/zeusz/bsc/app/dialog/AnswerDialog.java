package com.zeusz.bsc.app.dialog;

import android.app.Activity;

import com.zeusz.bsc.app.R;
import com.zeusz.bsc.app.ui.ViewManager;
import com.zeusz.bsc.core.Localization;
import com.zeusz.bsc.core.Object;


public class AnswerDialog extends GameDialog {

    protected AnswerDialog(Activity ctx, boolean answer) {
        super(
            ctx,
            answer ? R.drawable.positive_feedback : R.drawable.negative_feedback,
            Localization.localize(answer ? "word.yes" : "word.no"),
            null
        );
    }

    public AnswerDialog(Activity ctx, String question, boolean answer) {
        this(ctx, answer);
        setMessage(question);
    }

    public AnswerDialog(Activity ctx, Object object, boolean answer) {
        this(ctx, answer);
        setView(ViewManager.displayObject(ctx, object));
    }

}
