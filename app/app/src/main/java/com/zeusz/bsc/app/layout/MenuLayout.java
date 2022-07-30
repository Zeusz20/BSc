package com.zeusz.bsc.app.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;


public class MenuLayout extends LinearLayout {

    public MenuLayout(Context context) {
        super(context);
    }

    public MenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MenuLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void addViews(View... children) {
        for(View child: children) addView(child);
    }

}
