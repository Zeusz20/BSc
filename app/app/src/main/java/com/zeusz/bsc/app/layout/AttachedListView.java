package com.zeusz.bsc.app.layout;

import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.zeusz.bsc.app.R;


public class AttachedListView extends ListView {

    protected AlertDialog dialog;

    public AttachedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        setBackgroundResource(R.drawable.adapter_bg);
    }

    public void attach(AlertDialog dialog) {
        setBackground(null);  // no background when displayed in an alert dialog
        this.dialog = dialog;
    }

}
