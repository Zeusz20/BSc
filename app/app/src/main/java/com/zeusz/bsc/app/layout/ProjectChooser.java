package com.zeusz.bsc.app.layout;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.zeusz.bsc.app.network.Game;
import com.zeusz.bsc.app.adapter.ProjectListAdapter;

import java.io.File;
import java.util.Arrays;


public class ProjectChooser extends ListView {

    public ProjectChooser(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProjectChooser(Activity ctx) {
        this(ctx, (AttributeSet) null);

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
