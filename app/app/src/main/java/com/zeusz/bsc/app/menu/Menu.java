package com.zeusz.bsc.app.menu;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zeusz.bsc.app.R;
import com.zeusz.bsc.core.Localization;

import java.util.Locale;


public final class Menu {

    // menu ids
    public static final int MAIN_MENU = 0;
    public static final int NEW_GAME_MENU = 1;
    public static final int SCENE_MENU = 2;

    private static void init(Activity ctx) {
        Integer id;
        Locale locale = Localization.getLocale();

        try {
            // init title
            ImageView title = ctx.findViewById(R.id.title_image);
            id = (Integer) R.drawable.class.getField(locale.getLanguage() + "_title").get(null);
            if(id != null) title.setImageResource(id);

            // init language flag
            ImageView flag = ctx.findViewById(R.id.lang);
            id = (Integer) R.drawable.class.getField(locale.getLanguage() + "_flag").get(null);
            if(id != null) flag.setImageResource(id);
        }
        catch(Exception e) {
            // resource not available
        }
    }

    public static void initMenu(Activity ctx, int menuID) {
        init(ctx);

        LinearLayout controls = ctx.findViewById(R.id.controls);

        switch(menuID) {
            case MAIN_MENU:
                controls.addView(new MenuButton(ctx, Localization.localize("menu.new_game"), MenuEvents.NEW_GAME));
                controls.addView(new MenuButton(ctx, Localization.localize("menu.download"), MenuEvents.DOWNLOAD));
                controls.addView(new MenuButton(ctx, Localization.localize("menu.exit_game"), MenuEvents.EXIT_GAME));
                break;

            case NEW_GAME_MENU:
                break;

            case SCENE_MENU:
                break;
        }
    }

}
