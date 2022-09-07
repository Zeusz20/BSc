package com.zeusz.bsc.app.dialog;

import android.app.Activity;

import com.zeusz.bsc.app.MainActivity;
import com.zeusz.bsc.app.ui.ViewManager;
import com.zeusz.bsc.app.util.Dictionary;
import com.zeusz.bsc.core.Localization;
import com.zeusz.bsc.core.Object;


public class GuessDialog extends GameDialog {

    public GuessDialog(Activity ctx, Object object) {
        super(ctx);

        boolean isGuessed = ((MainActivity) ctx).getGameClient().getGame().getObject().equals(object);

        setCancelable(false);
        setView(ViewManager.displayObject(ctx, object));
        setTitle(Localization.localize("game.guess_object"));

        setPositiveButton(Localization.localize(isGuessed ? "word.yes" : "word.no"), (dialog, which) -> {
            ((MainActivity) ctx).getGameClient().sendJSON(new Dictionary()
                .put("object", object.getName())
                .put("answer", isGuessed)
            );
        });

        setOnDismissListener(dialog -> {
            // opponent guessed correctly
            if(isGuessed) new WinDialog(ctx, false).show();
        });
    }

}
