package com.zeusz.bsc.app.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import com.zeusz.bsc.app.ui.Menu;
import com.zeusz.bsc.core.Localization;

import java.util.Locale;


public class LangButton extends MenuButton {

    public LangButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LangButton(Activity ctx, Locale locale) {
        super(ctx, locale.getDisplayLanguage(locale), () -> {
            Localization.load(locale);
            Menu.show(ctx);
        });
    }

}
