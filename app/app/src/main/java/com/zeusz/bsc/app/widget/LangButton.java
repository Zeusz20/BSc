package com.zeusz.bsc.app.widget;

import android.app.Activity;

import com.zeusz.bsc.app.Menu;
import com.zeusz.bsc.core.Localization;

import java.util.Locale;


public class LangButton extends MenuButton {

    public LangButton(Activity ctx, Locale locale) {
        super(ctx, locale.getDisplayLanguage(locale), () -> {
            Localization.load(locale);
            Menu.show(ctx);
        });
    }

}
