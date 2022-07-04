package com.zeusz.bsc.app.menu;

import android.app.Activity;

import androidx.appcompat.widget.AppCompatButton;

import com.zeusz.bsc.app.MainActivity;
import com.zeusz.bsc.core.Localization;

import java.util.Locale;


public class LangButton extends AppCompatButton {

    public LangButton(Activity ctx, Locale locale) {
        super(ctx);
        setText(locale.getDisplayLanguage(locale));
        setOnClickListener(listener -> {
            Localization.load(locale);
            Menu.show(ctx, Menu.MAIN_MENU);
        });
    }

}
