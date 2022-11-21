package com.zeusz.bsc.app.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatEditText;

import com.zeusz.bsc.app.R;


public class TextInput extends AppCompatEditText {

    public TextInput(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextInput(Activity ctx) {
        this(ctx, (AttributeSet) null);
        setId(R.id.text_input);
        setTextColor(Color.WHITE);
        setTextAlignment(TEXT_ALIGNMENT_CENTER);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 24.0f);
    }

}
