package com.zeusz.bsc.app.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup.MarginLayoutParams;

import androidx.appcompat.widget.AppCompatButton;

import com.zeusz.bsc.app.R;


public class MenuButton extends AppCompatButton {

    public MenuButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MenuButton(Activity ctx, String text, Runnable onClick) {
        this(ctx, (AttributeSet) null);

        MarginLayoutParams layoutParams;
        layoutParams = new MarginLayoutParams(MarginLayoutParams.MATCH_PARENT, MarginLayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(8, 8, 8, 8);

        setLayoutParams(layoutParams);
        setBackgroundResource(R.drawable.neutral_btn_bg);
        setOnClickListener(root -> onClick.run());
        setTextColor(Color.WHITE);
        setText(text);
    }

}
