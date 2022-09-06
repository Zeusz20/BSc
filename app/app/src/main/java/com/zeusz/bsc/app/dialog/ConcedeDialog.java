package com.zeusz.bsc.app.dialog;

import android.app.Activity;

import com.zeusz.bsc.app.MainActivity;
import com.zeusz.bsc.app.R;
import com.zeusz.bsc.app.ui.ViewManager;
import com.zeusz.bsc.core.Localization;


public class ConcedeDialog extends GameDialog {

    public ConcedeDialog(Activity ctx) {
        super(ctx, R.drawable.warning_48dp, Localization.localize("game.concede_caption"), Localization.localize("game.confirm_concede"));
        setNeutralButton(Localization.localize("word.no"), GameDialog.DISMISS);
        setPositiveButton(Localization.localize("word.yes"), (dialog, which) -> concedeGame(ctx));
    }

    private void concedeGame(Activity ctx) {
        ((MainActivity) ctx).setGameClient(null);   // disconnect from game
        ViewManager.show(ctx, ViewManager.MAIN_MENU);
    }

}
