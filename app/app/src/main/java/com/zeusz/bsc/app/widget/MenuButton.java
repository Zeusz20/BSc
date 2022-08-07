package com.zeusz.bsc.app.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;

import androidx.appcompat.widget.AppCompatButton;


public class MenuButton extends AppCompatButton {

    public MenuButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MenuButton(Activity ctx, String text, Runnable onClick) {
        this(ctx, (AttributeSet) null);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        setOnClickListener(listener -> onClick.run());
        setText(text);
    }

}
