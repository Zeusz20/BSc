package com.zeusz.bsc.app.menu;

import android.app.Activity;
import android.widget.Button;
import android.widget.LinearLayout;

import com.zeusz.bsc.app.widget.MenuButton;
import com.zeusz.bsc.core.Localization;


public class MainMenu extends Menu {

    public MainMenu(Activity ctx, LinearLayout header, LinearLayout body, LinearLayout footer) {
        super(ctx, header, body, footer);
    }

    @Override
    protected void buildHeader(Activity ctx, LinearLayout header) {
        header.addView(MenuBuilder.getTitleImage(ctx));
    }

    @Override
    protected void buildBody(Activity ctx, LinearLayout body) {
        MenuButton newGameBtn = new MenuButton(ctx, Localization.localize("menu.new_game"));
        MenuButton downloadBtn = new MenuButton(ctx, Localization.localize("menu.download"));
        MenuButton optionsBtn = new MenuButton(ctx, Localization.localize("menu.options"));

        newGameBtn.setOnClickListener(listener -> MenuBuilder.build(ctx, GameMenu.class));
        downloadBtn.setOnClickListener(listener -> MenuBuilder.build(ctx, DownloadMenu.class));
        optionsBtn.setOnClickListener(listener -> MenuBuilder.build(ctx, OptionsMenu.class));

        body.addView(newGameBtn);
        body.addView(downloadBtn);
        body.addView(optionsBtn);
    }

    @Override
    protected void buildFooter(Activity ctx, LinearLayout footer) {
        Button button = new Button(ctx);
        button.setText(Localization.localize("menu.exit"));
        button.setOnClickListener(listener -> ctx.finish());

        footer.addView(button);
    }

}
