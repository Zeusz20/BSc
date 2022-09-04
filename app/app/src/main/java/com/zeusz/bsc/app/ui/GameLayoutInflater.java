package com.zeusz.bsc.app.ui;

import android.app.Activity;
import android.view.View;

import com.zeusz.bsc.app.R;


public final class GameLayoutInflater {

    private GameLayoutInflater() { }

    public static final int SELECTED_OBJECT_LAYOUT = 0;

    public static View inflate(Activity ctx, int layoutId) {
        View view = View.inflate(ctx, layoutId, null);

        switch(layoutId) {
            case R.layout.selected_object:
                break;
        }

        return view;
    }

}
