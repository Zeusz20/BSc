package com.zeusz.bsc.app.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;

import com.zeusz.bsc.app.R;


public class GameDialog extends AlertDialog.Builder {

    protected static OnClickListener DISMISS = (dialog, which) -> dialog.dismiss();

    /* Class fields and methods */
    private final Activity ctx;

    protected GameDialog(Activity ctx) {
        super(ctx);
        this.ctx = ctx;
    }

    public GameDialog(Activity ctx, String message) {
        this(ctx);
        setMessage(message);
        setPositiveButton(R.string.ok, GameDialog.DISMISS);
    }

    public GameDialog(Activity ctx, int icon, String title, String message) {
        this(ctx, message);
        setIcon(icon);
        setTitle(title);
    }

    @Override
    public AlertDialog show() {
        ctx.runOnUiThread(super::show);
        return null;
    }

}
