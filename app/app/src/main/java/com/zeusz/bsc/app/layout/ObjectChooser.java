package com.zeusz.bsc.app.layout;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.zeusz.bsc.app.R;
import com.zeusz.bsc.app.adapter.ObjectListAdapter;
import com.zeusz.bsc.core.Object;
import com.zeusz.bsc.core.Project;


public class ObjectChooser extends ListView {

    public ObjectChooser(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObjectChooser(Activity ctx, Project project) {
        this(ctx, (AttributeSet) null);
        setAdapter(new ObjectListAdapter(ctx, project.getItemList(Object.class)));
        setClickable(true);
        setOnItemClickListener(null);   // TODO
    }

    /** Used at the beginning of the game. Selects the player's object. */
    public void selectClick(View view, int position) {

    }

    /** Used while in game. Clicking on an object item will display it's attributes. */
    public void detailClick(View view, int position) {

    }

}
