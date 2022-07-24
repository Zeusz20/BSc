package com.zeusz.bsc.app.widget;

import android.app.Activity;

import com.zeusz.bsc.app.menu.MainMenu;
import com.zeusz.bsc.app.menu.MenuBuilder;
import com.zeusz.bsc.core.Localization;

import java.util.Locale;


public class LangButton extends MenuButton {

    public LangButton(Activity ctx, Locale locale) {
        super(ctx, locale.getDisplayLanguage(locale));

        setOnClickListener(listener -> {
            Localization.load(locale);
            MenuBuilder.build(ctx, MainMenu.class);
        });
    }

}
