package com.zeusz.bsc.app.dialog;

import android.app.Activity;

import androidx.annotation.Nullable;

import com.zeusz.bsc.app.MainActivity;
import com.zeusz.bsc.app.R;
import com.zeusz.bsc.app.network.GameClient;
import com.zeusz.bsc.app.ui.ViewManager;
import com.zeusz.bsc.core.Localization;
import com.zeusz.bsc.core.Object;


public class AnswerDialog extends GameDialog {

    protected AnswerDialog(Activity ctx, @Nullable Boolean answer) {
        super(
            ctx,
            (answer == null) ? R.drawable.neutral_feedback : answer ? R.drawable.positive_feedback : R.drawable.negative_feedback,
            Localization.localize((answer == null) ? "word.maybe" : answer ? "word.yes" : "word.no"),
            null
        );
    }

    public AnswerDialog(Activity ctx, String question, Boolean answer) {
        this(ctx, answer);
        setMessage(question);
    }

    public AnswerDialog(Activity ctx, Object object, Boolean answer) {
        this(ctx, answer);
        setView(ViewManager.displayObject(ctx, object));

        // check if player won
        if(answer != null && answer) {
            ((MainActivity) ctx).getGameClient().setState(GameClient.State.EXIT);

            setPositiveButton(R.string.ok, (dialog, which) -> {
                dialog.dismiss();
                new WinDialog(ctx, true).show();
            });
        }
    }

}
