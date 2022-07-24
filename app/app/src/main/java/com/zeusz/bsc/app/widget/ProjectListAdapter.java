package com.zeusz.bsc.app.widget;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zeusz.bsc.app.R;
import com.zeusz.bsc.core.Localization;
import com.zeusz.bsc.core.Project;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;


public class ProjectListAdapter extends ArrayAdapter<File> {

    private Activity ctx;

    public ProjectListAdapter(Activity ctx, File[] projects) {
        super(ctx, R.layout.project_item, projects);
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null)
            convertView = ctx.getLayoutInflater().inflate(R.layout.project_item, parent, false);

        Project project = getProject(position);

        if(project != null) {
            TextView nameView = convertView.findViewById(R.id.project_item_name);
            TextView descriptionView = convertView.findViewById(R.id.project_item_description);
            TextView authorView = convertView.findViewById(R.id.project_item_author);

            String name = getString(project.getName(), Localization.localize("project.unnamed"));
            String description = project.getDescription();
            String author = Localization.prompt("project.author") + getString(project.getAuthor(), Localization.localize("project.unknown"));

            nameView.setText(name);
            descriptionView.setText(description);
            authorView.setText(author);
        }

        return convertView;
    }

    private Project getProject(int position) {
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
