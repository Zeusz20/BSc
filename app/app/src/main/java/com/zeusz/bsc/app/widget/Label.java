package com.zeusz.bsc.app.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;


public class Label extends AppCompatTextView {

    public Label(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Label(Activity ctx, String text) {
        this(ctx, (AttributeSet) null);
        setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 24.0f);
        setTextColor(Color.WHITE);
        setText(text);
    }

}
