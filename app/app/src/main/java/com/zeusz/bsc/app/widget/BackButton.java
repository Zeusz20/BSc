package com.zeusz.bsc.app.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import com.zeusz.bsc.app.MainActivity;
import com.zeusz.bsc.app.R;
import com.zeusz.bsc.app.ui.Menu;
import com.zeusz.bsc.core.Localization;


public class BackButton extends MenuButton {

    public BackButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BackButton(Activity ctx, int previousMenuID) {
        super(ctx, Localization.localize("menu.back"), () -> {
            ((MainActivity) ctx).destroyGameClient();
            Menu.show(ctx, previousMenuID);
        });

        setId(R.id.back_btn);
    }

}
