package com.zeusz.bsc.app.widget;

import android.app.Activity;
import android.widget.LinearLayout.LayoutParams;

import androidx.appcompat.widget.AppCompatImageView;

import com.zeusz.bsc.app.R;
import com.zeusz.bsc.core.Localization;


public final class Title extends AppCompatImageView {

    public Title(Activity ctx) {
        super(ctx);
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
