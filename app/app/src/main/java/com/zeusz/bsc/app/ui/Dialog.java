package com.zeusz.bsc.app.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.widget.Toast;

import com.zeusz.bsc.app.MainActivity;
import com.zeusz.bsc.core.Localization;


public final class Dialog {

    private Dialog() { }

    public static void toast(Context context, String message) {
        ((Activity) context).runOnUiThread(() -> {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        });
    }

    public static void error(Activity ctx, String unlocalized) {
        ctx.runOnUiThread(() -> {
            buildDialog(ctx, Localization.localize(unlocalized), DialogEvents.DISMISS, "OK").show();
        });
    }

    public static void show(Activity ctx) {
        // TODO
        //  in game dialogs between clients
    }

    public static AlertDialog buildDialog(Activity ctx, String message, OnClickListener event, String buttonText) {
        AlertDialog dialog =  new AlertDialog.Builder(ctx)
                .setMessage(message)
                .setPositiveButton(buttonText, event)
                .create();

        dialog.setOwnerActivity(ctx);
        return dialog;
    }

}
