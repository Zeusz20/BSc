package com.zeusz.bsc.app.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import com.zeusz.bsc.app.Menu;
import com.zeusz.bsc.core.Localization;


public class BackButton extends MenuButton {

    public BackButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BackButton(Activity ctx, int previousMenuID) {
        super(ctx, Localization.localize("menu.back"), () -> Menu.show(ctx, previousMenuID));
    }

}
