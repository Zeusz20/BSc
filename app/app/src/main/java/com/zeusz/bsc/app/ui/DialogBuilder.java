package com.zeusz.bsc.app.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.zeusz.bsc.app.MainActivity;
import com.zeusz.bsc.app.R;
import com.zeusz.bsc.app.adapter.AttributeListAdapter;
import com.zeusz.bsc.app.adapter.ProjectListAdapter;
import com.zeusz.bsc.core.Localization;
import com.zeusz.bsc.core.Object;
import com.zeusz.bsc.core.Project;


public final class DialogBuilder extends AlertDialog.Builder {

    /* Static functionalities */
    private static final DialogInterface.OnClickListener DISMISS = (dialog, id) -> dialog.dismiss();

    public static void toast(Context context, String message) {
        ((Activity) context).runOnUiThread(() -> {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        });
    }

    /** Displays a basic dialog window with an error message. */
    public static void error(Activity ctx, String unlocalized) {
        ctx.runOnUiThread(() -> {
            new DialogBuilder(ctx)
                    .setMessage(Localization.localize(unlocalized))
                    .setPositiveButton(R.string.ok, DialogBuilder.DISMISS)
                    .create().show();
        });
    }

    public static void delete(ProjectListAdapter adapter, Project project) {
        Activity ctx = (Activity) adapter.getContext();

        ctx.runOnUiThread(() -> {
            new DialogBuilder(ctx)
                    .setIcon(R.drawable.warning_48dp)
                    .setTitle(Localization.localize("menu.delete_caption"))
                    .setMessage(String.format(Localization.localize("menu.delete_description"), project.getName()))
                    .setNeutralButton(Localization.localize("word.no"), DialogBuilder.DISMISS)
                    .setPositiveButton(Localization.localize("word.yes"), (dialog, id) -> {
                        if(project.getSource().delete()) {
                            DialogBuilder.toast(ctx, Localization.localize("menu.delete_successful"));
                            adapter.remove(project.getSource());    // remove file from adapter
                            adapter.notifyDataSetChanged();     // refresh view
                        }
                    })
                    .create().show();
        });
    }

    public static void concede(Activity ctx) {
        ctx.runOnUiThread(() -> {
            new DialogBuilder(ctx)
                    .setIcon(R.drawable.warning_48dp)
                    .setTitle(Localization.localize("game.concede_caption"))
                    .setMessage(Localization.localize("game.confirm_concede"))
                    .setNeutralButton(Localization.localize("word.no"), DialogBuilder.DISMISS)
                    .setPositiveButton(Localization.localize("word.yes"), (dialog, id) -> {
                        ((MainActivity) ctx).setGameClient(null);   // disconnect from game
                        ViewManager.show(ctx, ViewManager.MAIN_MENU);
                    })
                    .create().show();
        });
    }

    public static void showAttributeList(Activity ctx, Object object) {
        ctx.runOnUiThread(() -> {
            new DialogBuilder(ctx)
                    .setTitle(Localization.localize("game.object_attributes"))
                    .setAdapter(new AttributeListAdapter(ctx, object.getAttributes()), null)
                    .setPositiveButton(R.string.ok, DialogBuilder.DISMISS)
                    .create().show();
        });
    }

    public static void show(Activity ctx) {
        // TODO
        //  in game dialogs between clients
    }

    /* Class fields and methods */
    private final Activity ctx;

    private DialogBuilder(Activity ctx) {
        super(ctx);
        this.ctx = ctx;
    }

    @Override
    public AlertDialog create() {
        AlertDialog dialog = super.create();
        dialog.setOwnerActivity(ctx);
        return dialog;
    }

}
