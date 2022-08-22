package com.zeusz.bsc.app.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import com.zeusz.bsc.app.MainActivity;
import com.zeusz.bsc.app.R;
import com.zeusz.bsc.app.ui.Menu;
import com.zeusz.bsc.core.Localization;


public class BackButton extends MenuButton {

    protected int previousMenuId;

    public BackButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BackButton(Activity ctx, int previousMenuId) {
        super(ctx, Localization.localize("menu.back"), () -> {
            ((MainActivity) ctx).setGameClient(null);
            Menu.show(ctx, previousMenuId);
        });

        this.previousMenuId = previousMenuId;
        setId(R.id.back_btn);
    }

    public int getPreviousMenuId() { return previousMenuId; }

}
