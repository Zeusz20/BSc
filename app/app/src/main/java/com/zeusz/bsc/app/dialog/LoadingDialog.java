package com.zeusz.bsc.app.dialog;

import android.app.Activity;
import android.app.AlertDialog;

import com.zeusz.bsc.app.widget.LoadingIcon;
import com.zeusz.bsc.core.Localization;


public class LoadingDialog extends GameDialog {

    private static AlertDialog dialog;

    public static void display(Activity ctx) {
        ctx.runOnUiThread(() -> {
            dialog = new LoadingDialog(ctx).create();
            dialog.show();
        });
    }

    public static void hide(Activity ctx) {
        ctx.runOnUiThread(() -> {
            if(dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
        });
    }

    private LoadingDialog(Activity ctx) {
        super(ctx);
        setCancelable(false);
        setMessage(Localization.localize("game.download"));
        setView(new LoadingIcon(ctx));
    }

}
