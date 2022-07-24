package com.zeusz.bsc.app.widget;

import android.app.Activity;
import android.view.ViewGroup.LayoutParams;

import androidx.appcompat.widget.AppCompatButton;


public class MenuButton extends AppCompatButton {

    public MenuButton(Activity ctx, String text) {
        super(ctx);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        setText(text);
    }

}