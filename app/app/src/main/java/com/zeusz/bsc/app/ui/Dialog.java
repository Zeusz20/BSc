package com.zeusz.bsc.app.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import com.zeusz.bsc.app.R;
import com.zeusz.bsc.app.adapter.ProjectListAdapter;
import com.zeusz.bsc.core.Localization;
import com.zeusz.bsc.core.Project;


public final class Dialog {

    private Dialog() { }

    public static void toast(Context context, String message) {
        ((Activity) context).runOnUiThread(() -> {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        });
    }

    /** Displays a warning dialog window before deleting a project. */
    public static void warning(ProjectListAdapter adapter, Project project) {
        Activity ctx = (Activity) adapter.getContext();

        ctx.runOnUiThread(() -> {
            AlertDialog alert = new AlertDialog.Builder(ctx)
                    .setIcon(R.drawable.warning_48dp)
                    .setTitle(Localization.localize("menu.delete_caption"))
                    .setMessage(String.format(Localization.localize("menu.delete_description"), project.getName()))
                    .setNeutralButton(Localization.localize("word.no"), (dialog, id) -> dialog.dismiss())
                    .setPositiveButton(Localization.localize("word.yes"), (dialog, id) -> {
                        if(project.getSource().delete()) {
                            toast(ctx, Localization.localize("menu.delete_successful"));
                            adapter.remove(project.getSource());    // remove file from adapter
                            adapter.notifyDataSetChanged();     // refresh view
                        }
                    })
                    .create();

            alert.setOwnerActivity(ctx);
            alert.show();
        });
    }

    /** Displays a basic dialog window with an error message. */
    public static void error(Activity ctx, String unlocalized) {
        ctx.runOnUiThread(() -> {
            AlertDialog alert = new AlertDialog.Builder(ctx)
                    .setMessage(Localization.localize(unlocalized))
                    .setPositiveButton(R.string.ok, (dialog, id) -> dialog.dismiss())
                    .create();

            alert.setOwnerActivity(ctx);
            alert.show();
        });
    }

    public static void show(Activity ctx) {
        // TODO
        //  in game dialogs between clients
    }

}
