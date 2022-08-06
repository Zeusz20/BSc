package com.zeusz.bsc.app.layout;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;


public class MenuLayout extends LinearLayout {

    public MenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MenuLayout(Activity ctx, int orientation, int height) {
        this(ctx, null);
        setOrientation(orientation);
        setHeight(height);
    }

    public MenuLayout(Activity ctx, int orientation) {
        this(ctx, orientation, LayoutParams.WRAP_CONTENT);
        setOrientation(orientation);
    }

    public void setHeight(int dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        int height = (dp < 0) ? LayoutParams.WRAP_CONTENT : (int) (density * dp);

        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, height));
    }

    public void addViews(View... children) {
        for(View child: children) addView(child);
    }

}
