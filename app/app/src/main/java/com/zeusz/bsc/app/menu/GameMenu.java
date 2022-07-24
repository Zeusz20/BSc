package com.zeusz.bsc.app.menu;

import android.app.Activity;
import android.widget.LinearLayout;

import com.zeusz.bsc.app.widget.MenuButton;
import com.zeusz.bsc.core.Localization;


public class GameMenu extends Menu {

    public GameMenu(Activity ctx, LinearLayout header, LinearLayout body, LinearLayout footer) {
        super(ctx, header, body, footer);
    }

    @Override
    protected void buildHeader(Activity ctx, LinearLayout header) {
        header.addView(MenuBuilder.getTitleImage(ctx));
    }

    @Override
    protected void buildBody(Activity ctx, LinearLayout body) {
        MenuButton onePlayerBtn = new MenuButton(ctx, Localization.localize("menu.1_player"));
        MenuButton twoPlayerBtn = new MenuButton(ctx, Localization.localize("menu.2_player"));

        // TODO: 1 player mode
        twoPlayerBtn.setOnClickListener(listener -> MenuBuilder.build(ctx, MultiplayerMenu.class));

        body.addView(onePlayerBtn);
        body.addView(twoPlayerBtn);
    }

    @Override
    protected void buildFooter(Activity ctx, LinearLayout footer) {
        footer.addView(MenuBuilder.getBackButton(ctx, MainMenu.class));
    }

}
