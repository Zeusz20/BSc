package com.zeusz.bsc.app.layout;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.zeusz.bsc.app.adapter.ProjectListAdapter;
import com.zeusz.bsc.app.network.GameClient;
import com.zeusz.bsc.app.util.IOManager;


public class ProjectChooser extends ListView {

    public ProjectChooser(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProjectChooser(Activity ctx, boolean showDelete) {
        this(ctx, (AttributeSet) null);

        ProjectListAdapter adapter = new ProjectListAdapter(ctx, IOManager.listProjects(ctx));
        setAdapter(adapter);
        setClickable(true);

        setOnItemClickListener((parent, view, position, id) -> {
            GameClient.createGame(ctx, adapter.getProject(position));
        });
    }

}
