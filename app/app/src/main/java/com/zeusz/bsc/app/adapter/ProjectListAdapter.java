package com.zeusz.bsc.app.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeusz.bsc.app.R;
import com.zeusz.bsc.app.ui.DialogBuilder;
import com.zeusz.bsc.app.util.IOManager;
import com.zeusz.bsc.core.Localization;
import com.zeusz.bsc.core.Project;

import java.io.File;
import java.util.List;


public class ProjectListAdapter extends Adapter<File> {

    public ProjectListAdapter(Activity ctx, List<File> projects) {
        super(ctx, R.layout.project_item, projects);
    }

    @Override
    protected View render(View view, int position) {
        File file = getItem(position);
        Project project = getProject(position);

        if(project != null) {
            // add project info
            TextView nameView = view.findViewById(R.id.project_item_name);
            TextView descriptionView = view.findViewById(R.id.project_item_description);
            TextView authorView = view.findViewById(R.id.project_item_author);

            String name = getString(project.getName(), Localization.localize("project.unnamed"));
            String description = getString(project.getDescription(), "—");
            String author = getString(project.getAuthor(), Localization.localize("project.unknown"));

            nameView.setText(name);
            descriptionView.setText(description);
            authorView.setText(String.format("%s %s", Localization.prompt("project.author"), author)); // couldn't concat strings, had to use format

            // add file info and functionality
            TextView dateView = view.findViewById(R.id.project_date);
            ImageView deleteBtn = view.findViewById(R.id.delete_button);

            dateView.setText(new java.sql.Date(file.lastModified()).toString());
            deleteBtn.setOnClickListener(listener -> DialogBuilder.delete(this, project));
        }

        return view;
    }

    public Project getProject(int position) {
        return IOManager.loadProject(getItem(position));
    }

    public String getString(String string, String defaultString) {
        return string.equals("") ? defaultString : string;
    }

}
