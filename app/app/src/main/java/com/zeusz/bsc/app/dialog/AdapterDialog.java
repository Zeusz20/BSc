package com.zeusz.bsc.app.dialog;

import android.app.Activity;
import android.app.AlertDialog;

import com.zeusz.bsc.app.layout.AttachedListView;


public abstract class AdapterDialog extends GameDialog {

    protected AttachedListView listView;

    protected AdapterDialog(Activity ctx, AttachedListView listView) {
        super(ctx);
        this.listView = listView;
        setView(listView);
    }

    @Override
    public AlertDialog create() {
        AlertDialog dialog = super.create();
        listView.attach(dialog);
        return dialog;
    }

}
