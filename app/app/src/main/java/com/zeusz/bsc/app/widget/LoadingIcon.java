package com.zeusz.bsc.app.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;


public class LoadingIcon extends ProgressBar {

    public LoadingIcon(Context context, AttributeSet attr) {
        super(context, attr);
    }

    public LoadingIcon(Activity ctx) {
        this(ctx, null);
        setIndeterminate(true);
    }

}
