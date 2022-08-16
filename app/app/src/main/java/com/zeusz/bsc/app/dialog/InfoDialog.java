package com.zeusz.bsc.app.dialog;

import android.app.Activity;
import android.app.AlertDialog;

import com.zeusz.bsc.app.R;


public class InfoDialog extends AlertDialog.Builder {

    public InfoDialog(Activity ctx, String message) {
        super(ctx);
        setMessage(message);
        setPositiveButton(R.string.ok, DialogEvents.DISMISS);
    }

}
