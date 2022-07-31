package com.zeusz.bsc.app.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.zeusz.bsc.app.R;
import com.zeusz.bsc.core.Localization;
import com.zeusz.bsc.core.Project;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;


public class ProjectListAdapter extends Adapter<File> {

    public ProjectListAdapter(Activity ctx, List<File> projects) {
        super(ctx, R.layout.project_item, projects);
        this.ctx = ctx;
    }

    @Override
    protected View render(View view, int position) {
        Project project = getProject(position);

        if(project != null) {
            TextView nameView = view.findViewById(R.id.project_item_name);
            TextView descriptionView = view.findViewById(R.id.project_item_description);
            TextView authorView = view.findViewById(R.id.project_item_author);

            String name = getString(project.getName(), Localization.localize("project.unnamed"));
            String description = getString(project.getDescription(), "—");
            String author = getString(project.getAuthor(), Localization.localize("project.unknown"));

            nameView.setText(name);
            descriptionView.setText(description);
            authorView.setText(String.format("%s: %s", Localization.prompt("project.author"), author)); // couldn't concat strings, had to use format
        }

        return view;
    }

    public Project getProject(int position) {
        File file = getItem(position);

        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Project) ois.readObject();
        }
        catch(Exception e) {
            // couldn't read project
            return null;
        }
    }

    public String getString(String string, String defaultString) {
        return string.equals("") ? defaultString : string;
    }

}
