package com.zeusz.bsc.app.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout.LayoutParams;

import androidx.appcompat.widget.AppCompatImageView;

import com.zeusz.bsc.app.R;
import com.zeusz.bsc.core.Localization;


public final class Title extends AppCompatImageView {

    public Title(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Title(Activity ctx) {
        this(ctx, (AttributeSet) null);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 256));

        try {
            String resource = Localization.getLocale().getLanguage() + "_title";
            Integer id = (Integer) R.drawable.class.getField(resource).get(null);
            if(id != null) setImageResource(id);
        }
        catch(Exception e) {
            // drawable doesn't exist
        }
    }

}
