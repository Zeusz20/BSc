package com.zeusz.bsc.app;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout.LayoutParams;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.zeusz.bsc.app.layout.JSWebView;
import com.zeusz.bsc.app.layout.JoinGameForm;
import com.zeusz.bsc.app.layout.LanguageChooser;
import com.zeusz.bsc.app.layout.MenuLayout;
import com.zeusz.bsc.app.layout.ObjectChooser;
import com.zeusz.bsc.app.layout.ProjectChooser;
import com.zeusz.bsc.app.widget.BackButton;
import com.zeusz.bsc.app.widget.Label;
import com.zeusz.bsc.app.widget.MenuButton;
import com.zeusz.bsc.app.widget.Title;
import com.zeusz.bsc.core.Localization;


public final class Menu {

    private Menu() { }

    public static final int MAIN_MENU = 0;
    public static final int GAME_MENU = 1;
    public static final int PROJECTS_MENU = 2;
    public static final int OPTIONS_MENU = 3;
    public static final int DOWNLOAD_MENU = 4;

    public static void show(Activity ctx) {
        show(ctx, MAIN_MENU);
    }

    public static void show(Activity ctx, int menuID) {
        // menu setup
        ctx.setContentView(R.layout.menu_layout);

        ConstraintLayout root = ctx.findViewById(R.id.menu_root);
        MenuLayout header = ctx.findViewById(R.id.menu_header);
        MenuLayout body = ctx.findViewById(R.id.menu_body);
        MenuLayout footer = ctx.findViewById(R.id.menu_footer);

        header.removeAllViews();
        body.removeAllViews();
        footer.removeAllViews();

        // menu render
        switch(menuID) {
            case MAIN_MENU:
                header.addView(new Title(ctx));
                body.addViews(
                        new MenuButton(ctx, Localization.localize("menu.new_game"), () -> show(ctx, GAME_MENU)),
                        new MenuButton(ctx, Localization.localize("menu.download"), () -> show(ctx, DOWNLOAD_MENU)),
                        new MenuButton(ctx, Localization.localize("menu.options"), () -> show(ctx, OPTIONS_MENU))
                );
                footer.addView(new MenuButton(ctx, Localization.localize("menu.exit"), ctx::finish));
                break;

            case GAME_MENU:
                header.addView(new Title(ctx));
                body.addViews(
                        new MenuButton(ctx, Localization.localize("menu.create_game"), () -> show(ctx, PROJECTS_MENU)),
                        new MenuButton(ctx, Localization.localize("menu.join_game"), () -> {
                            body.removeAllViews();
                            body.addView(new JoinGameForm(ctx));
                        })
                );
                footer.addView(new BackButton(ctx, MAIN_MENU));
                break;

            case PROJECTS_MENU:
                ProjectChooser chooser = new ProjectChooser(ctx);
                LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

                params.setMargins(4, 0, 4, 0);
                chooser.setLayoutParams(params);

                header.addView(new Label(ctx, Localization.localize("menu.choose_project")));
                body.setGravity(Gravity.NO_GRAVITY);
                body.setBackgroundColor(Color.WHITE);
                body.addView(chooser);
                footer.addViews(new BackButton(ctx, GAME_MENU));
                break;

            case OPTIONS_MENU:
                header.addView(new Label(ctx, Localization.localize("menu.change_language")));
                body.addView(new LanguageChooser(ctx));
                footer.addView(new BackButton(ctx, MAIN_MENU));
                break;

            case DOWNLOAD_MENU:
                JSWebView browser = new JSWebView(ctx, "/user/search/?lang=" + Localization.getLocale().getLanguage());
                browser.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

                body.addView(browser);
                footer.addView(new BackButton(ctx, MAIN_MENU));
                break;
        }
    }

}
