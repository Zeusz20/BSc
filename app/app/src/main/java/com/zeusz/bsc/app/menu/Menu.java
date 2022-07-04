package com.zeusz.bsc.app.menu;

import android.app.Activity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zeusz.bsc.app.R;
import com.zeusz.bsc.core.Localization;

import java.util.Locale;


public final class Menu {

    // menu ids
    public static final int MAIN_MENU = 0;
    public static final int NEW_GAME_MENU = 1;
    public static final int OPTIONS_MENU = 2;

    public static void show(Activity ctx, int menuID) {
        ctx.setContentView(R.layout.menu_screen);
        setTitleImage(ctx);

        LinearLayout controls = ctx.findViewById(R.id.controls);
        controls.removeAllViews();

        switch(menuID) {
            case MAIN_MENU:
                controls.addView(new MenuButton(ctx, Localization.localize("menu.new_game"), MenuEvents.NEW_GAME));
                controls.addView(new MenuButton(ctx, Localization.localize("menu.download"), MenuEvents.DOWNLOAD));
                controls.addView(new MenuButton(ctx, Localization.localize("menu.options"), MenuEvents.OPTIONS));
                setBackBtnFunction(ctx, true);
                break;

            case NEW_GAME_MENU:
                controls.addView(new MenuButton(ctx, Localization.localize("menu.1_player"), MenuEvents.NEW_GAME));
                controls.addView(new MenuButton(ctx, Localization.localize("menu.2_player"), MenuEvents.NEW_GAME));
                setBackBtnFunction(ctx, false);
                break;

            case OPTIONS_MENU:
                LangChooserLayout layout = new LangChooserLayout(ctx);

                for(Locale locale: Localization.getSupportedLocales())
                    layout.addView(new LangButton(ctx, locale));

                controls.addView(layout);
                setBackBtnFunction(ctx, false);
                break;
        }
    }

    private static void setTitleImage(Activity ctx) {
        try {
            // set localized title image
            ImageView title = ctx.findViewById(R.id.title_image);
            Integer id = (Integer) R.drawable.class.getField(Localization.getLocale().getLanguage() + "_title").get(null);
            if(id != null) title.setImageResource(id);
        }
        catch(Exception e) {
            // resource not found
        }
    }

    private static void setBackBtnFunction(Activity ctx, boolean isExit) {
        Button backBtn = ctx.findViewById(R.id.back_button);
        String text = isExit ? "menu.exit" : "menu.back";

        backBtn.setText(Localization.localize(text));
        backBtn.setOnClickListener(listener -> {
            if(isExit) ctx.finish();
            else show(ctx, MAIN_MENU);
        });
    }

}
