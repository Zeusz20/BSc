package com.zeusz.bsc.app.layout;

import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;


public class AttachedListView extends ListView {

    protected AlertDialog dialog;

    public AttachedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void attach(AlertDialog dialog) {
        this.dialog = dialog;
    }

}
