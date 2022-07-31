package com.zeusz.bsc.app.layout;

import android.app.Activity;
import android.widget.ListView;

import com.zeusz.bsc.app.adapter.ObjectListAdapter;
import com.zeusz.bsc.core.Object;
import com.zeusz.bsc.core.Project;


public class ObjectChooser extends ListView {

    public ObjectChooser(Activity ctx, Project project) {
        super(ctx);
        setAdapter(new ObjectListAdapter(ctx, project.getItemList(Object.class)));
        setClickable(true);
        setOnItemClickListener(null);   // TODO
    }

}
