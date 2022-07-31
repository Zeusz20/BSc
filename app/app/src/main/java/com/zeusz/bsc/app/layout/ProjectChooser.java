package com.zeusz.bsc.app.layout;

import android.app.Activity;
import android.widget.ListView;

import com.zeusz.bsc.app.Game;
import com.zeusz.bsc.app.adapter.ProjectListAdapter;

import java.io.File;
import java.util.Arrays;


public class ProjectChooser extends ListView {

    public ProjectChooser(Activity ctx) {
        super(ctx);

        File[] files = ctx.getExternalFilesDir("projects").listFiles();

        if(files != null) {
            ProjectListAdapter adapter = new ProjectListAdapter(ctx, Arrays.asList(files));
            setAdapter(adapter);
            setClickable(true);
            setOnItemClickListener((parent, view, position, id) -> {
                new Game(ctx, adapter.getProject(position));
            });  // TODO
        }
    }

}
