package com.zeusz.bsc.app.dialog;

import android.app.Activity;

import com.zeusz.bsc.app.R;
import com.zeusz.bsc.app.adapter.ProjectListAdapter;
import com.zeusz.bsc.app.ui.ViewManager;
import com.zeusz.bsc.core.Localization;
import com.zeusz.bsc.core.Project;


public class DeleteDialog extends GameDialog {

    public DeleteDialog(Activity ctx, Project project, ProjectListAdapter adapter) {
        super(
            ctx,
            R.drawable.warning_48dp,
            Localization.localize("menu.delete_caption"),
            String.format(Localization.localize("menu.delete_description"), project.getName())
        );

        setNeutralButton(Localization.localize("word.no"), GameDialog.DISMISS);
        setPositiveButton(Localization.localize("word.yes"), (dialog, which) -> deleteProject(ctx, project, adapter));
    }

    private void deleteProject(Activity ctx, Project project, ProjectListAdapter adapter) {
        if(project.getSource().delete()) {
            ViewManager.toast(ctx, Localization.localize("menu.delete_successful"));
            adapter.remove(project.getSource());    // remove file from adapter
            adapter.notifyDataSetChanged();     // refresh view
        }
    }

}
