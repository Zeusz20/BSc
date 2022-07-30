package com.zeusz.bsc.app.widget;

import android.app.Activity;

import com.zeusz.bsc.app.Menu;
import com.zeusz.bsc.core.Localization;


public class BackButton extends MenuButton {

    public BackButton(Activity ctx, int previousMenuID) {
        super(ctx, Localization.localize("menu.back"), () -> Menu.show(ctx, previousMenuID));
    }

}
