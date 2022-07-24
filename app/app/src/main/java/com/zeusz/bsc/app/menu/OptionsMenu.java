package com.zeusz.bsc.app.menu;

import android.app.Activity;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.zeusz.bsc.app.widget.LangButton;
import com.zeusz.bsc.core.Localization;

import java.util.Locale;


public class OptionsMenu extends Menu {

    public OptionsMenu(Activity ctx, LinearLayout header, LinearLayout body, LinearLayout footer) {
        super(ctx, header, body, footer);
    }

    @Override
    protected void buildHeader(Activity ctx, LinearLayout header) {
        header.addView(MenuBuilder.getLabel(ctx, Localization.localize("menu.change_language")));
    }

    @Override
    protected void buildBody(Activity ctx, LinearLayout body) {
        LinearLayout languages = new LinearLayout(ctx);
        languages.setOrientation(LinearLayout.VERTICAL);
        languages.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 512));

        for(Locale locale: Localization.getSupportedLocales())
            languages.addView(new LangButton(ctx, locale));

        ScrollView wrapper = new ScrollView(ctx);
        wrapper.addView(languages);

        body.addView(wrapper);
    }

    @Override
    protected void buildFooter(Activity ctx, LinearLayout footer) {
        footer.addView(MenuBuilder.getBackButton(ctx, MainMenu.class));
    }

}
