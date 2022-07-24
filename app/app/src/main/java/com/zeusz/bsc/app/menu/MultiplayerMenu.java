package com.zeusz.bsc.app.menu;

import android.app.Activity;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zeusz.bsc.app.widget.MenuButton;
import com.zeusz.bsc.core.Localization;


public class MultiplayerMenu extends Menu {

    public MultiplayerMenu(Activity ctx, LinearLayout header, LinearLayout body, LinearLayout footer) {
        super(ctx, header, body, footer);
    }

    @Override
    protected void buildHeader(Activity ctx, LinearLayout header) {
        TextView playerID = MenuBuilder.getLabel(ctx, "12");
        playerID.setTextSize(TypedValue.COMPLEX_UNIT_SP, 64.0f);

        header.addView(playerID);
    }

    @Override
    protected void buildBody(Activity ctx, LinearLayout body) {
        MenuButton createGameBtn = new MenuButton(ctx, Localization.localize("menu.create_game"));
        MenuButton joinGameBtn = new MenuButton(ctx, Localization.localize("menu.join_game"));

        createGameBtn.setOnClickListener(listener -> MenuBuilder.build(ctx, ProjectsMenu.class));
        joinGameBtn.setOnClickListener(listener -> {
            // connect to other player via ID submenu
            body.removeAllViews();
            MenuButton connectBtn = new MenuButton(ctx, Localization.localize("menu.connect"));
            connectBtn.setOnClickListener(null);    // TODO: multiplayer connection
            body.addView(connectBtn);
        });

        body.addView(createGameBtn);
        body.addView(joinGameBtn);
    }

    @Override
    protected void buildFooter(Activity ctx, LinearLayout footer) {
        footer.addView(MenuBuilder.getBackButton(ctx, GameMenu.class));
    }

}
