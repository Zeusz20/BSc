package com.zeusz.bsc.app.layout;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.zeusz.bsc.app.widget.LangButton;
import com.zeusz.bsc.core.Localization;

import java.util.Locale;


public class LanguageChooser extends ScrollView {

    public LanguageChooser(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LanguageChooser(Activity ctx) {
        super(ctx);

        MenuLayout languages = new MenuLayout(ctx, LinearLayout.VERTICAL, 512);

        for(Locale locale: Localization.getSupportedLocales())
            languages.addView(new LangButton(ctx, locale));

        addView(languages);
    }

}
