package com.zeusz.bsc.app.layout;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.zeusz.bsc.app.adapter.AttributeListAdapter;
import com.zeusz.bsc.core.Attribute;

import java.util.List;


public class AttributeList extends ListView {

    public AttributeList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AttributeList(Activity ctx, List<Attribute> attributes) {
        super(ctx);
        setAdapter(new AttributeListAdapter(ctx, attributes));
    }

}
