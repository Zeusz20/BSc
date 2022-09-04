package com.zeusz.bsc.app.layout;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.zeusz.bsc.app.MainActivity;
import com.zeusz.bsc.app.adapter.ObjectListAdapter;
import com.zeusz.bsc.core.Object;

import java.util.List;


public class ObjectChooser extends ListView {

    public ObjectChooser(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObjectChooser(Activity ctx, List<Object> objects) {
        this(ctx, (AttributeSet) null);
        setAdapter(new ObjectListAdapter(ctx, objects));
        setClickable(true);
        setOnItemClickListener((parent, view, position, id) -> {
            Object object = (Object) getItemAtPosition(position);
            ((MainActivity) ctx).getGameClient().selectObject(object);
        });
    }

}
