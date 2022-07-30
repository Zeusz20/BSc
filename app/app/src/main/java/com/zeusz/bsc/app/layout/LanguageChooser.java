package com.zeusz.bsc.app.layout;

import android.app.Activity;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.zeusz.bsc.app.widget.LangButton;
import com.zeusz.bsc.core.Localization;

import java.util.Locale;


public class LanguageChooser extends ScrollView {

    public LanguageChooser(Activity ctx) {
        super(ctx);

        LinearLayout languages = new LinearLayout(ctx);
        languages.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 512));
        languages.setOrientation(LinearLayout.VERTICAL);

        for(Locale locale: Localization.getSupportedLocales())
            languages.addView(new LangButton(ctx, locale));

        addView(languages);
    }

}
