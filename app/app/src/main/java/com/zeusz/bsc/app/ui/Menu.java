package com.zeusz.bsc.app.ui;

import android.app.Activity;
import android.graphics.Color;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.zeusz.bsc.app.MainActivity;
import com.zeusz.bsc.app.R;
import com.zeusz.bsc.app.layout.JSWebView;
import com.zeusz.bsc.app.layout.LanguageChooser;
import com.zeusz.bsc.app.layout.MenuLayout;
import com.zeusz.bsc.app.layout.ProjectChooser;
import com.zeusz.bsc.app.network.GameClient;
import com.zeusz.bsc.app.widget.BackButton;
import com.zeusz.bsc.app.widget.Label;
import com.zeusz.bsc.app.widget.LoadingIcon;
import com.zeusz.bsc.app.widget.MenuButton;
import com.zeusz.bsc.app.widget.TextInput;
import com.zeusz.bsc.app.widget.Title;
import com.zeusz.bsc.core.Localization;


public final class Menu {

    private Menu() { }

    public static final int MAIN_MENU = 0;
    public static final int LOBBY_MENU = 1;
    public static final int PROJECTS_MENU = 2;
    public static final int OPTIONS_MENU = 3;
    public static final int DOWNLOAD_MENU = 4;
    public static final int WAITING_FOR_PLAYER = 5;

    public static void show(Activity ctx) {
        show(ctx, MAIN_MENU);
    }

    public static void show(Activity ctx, int menuId) {
        View menu = View.inflate(ctx, R.layout.menu_layout, null);

        ConstraintLayout root = menu.findViewById(R.id.menu_root);
        MenuLayout header = menu.findViewById(R.id.menu_header);
        MenuLayout body = menu.findViewById(R.id.menu_body);
        MenuLayout footer = menu.findViewById(R.id.menu_footer);

        render(ctx, menuId, root, header, body, footer);
        ctx.runOnUiThread(() -> ctx.setContentView(menu));
    }

    private static void render(Activity ctx, int menuId, ConstraintLayout root, MenuLayout header, MenuLayout body, MenuLayout footer) {
        switch(menuId) {
            case MAIN_MENU:
                header.addView(new Title(ctx));
                body.addViews(
                        new MenuButton(ctx, Localization.localize("menu.create_game"), () -> show(ctx, PROJECTS_MENU)),
                        new MenuButton(ctx, Localization.localize("menu.join_game"), () -> show(ctx, LOBBY_MENU)),
                        new MenuButton(ctx, Localization.localize("menu.download"), () -> show(ctx, DOWNLOAD_MENU)),
                        new MenuButton(ctx, Localization.localize("menu.options"), () -> show(ctx, OPTIONS_MENU))
                );
                footer.addView(new MenuButton(ctx, Localization.localize("menu.exit"), ctx::finish));
                break;

            case LOBBY_MENU:
                header.addView(new Title(ctx));
                body.addViews(
                        new Label(ctx, Localization.localize("menu.enter_id"), 18.0f),
                        new TextInput(ctx),
                        new MenuButton(ctx, Localization.localize("menu.connect"), () -> {
                            Editable raw = ((TextInput) ctx.findViewById(R.id.text_input)).getText();
                            if(raw != null)
                                GameClient.joinGame(ctx, raw.toString().toUpperCase());
                        })
                );
                footer.addView(new BackButton(ctx, MAIN_MENU));
                break;

            case PROJECTS_MENU:
                ProjectChooser chooser = new ProjectChooser(ctx, false);
                LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

                params.setMargins(4, 0, 4, 0);
                chooser.setLayoutParams(params);

                header.addView(new Label(ctx, Localization.localize("menu.choose_project")));
                body.setGravity(Gravity.NO_GRAVITY);
                body.setBackgroundColor(Color.WHITE);
                body.addView(chooser);
                footer.addViews(new BackButton(ctx, MAIN_MENU));
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

            case WAITING_FOR_PLAYER:
                MainActivity activity = (MainActivity) ctx;

                header.addViews(
                        new Label(ctx, Localization.localize("game.your_id")),
                        new Label(ctx, activity.getGameClient().getId(), 48.0f)
                );
                body.addViews(
                        new Label(ctx, Localization.localize("game.waiting_for_player")),
                        new LoadingIcon(ctx)
                );
                footer.addView(new BackButton(ctx, PROJECTS_MENU));
                break;
        }
    }

}
