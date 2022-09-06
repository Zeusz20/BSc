package com.zeusz.bsc.app.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import com.zeusz.bsc.app.MainActivity;
import com.zeusz.bsc.app.R;
import com.zeusz.bsc.app.ui.ViewManager;
import com.zeusz.bsc.core.Localization;


public class BackButton extends MenuButton {

    public BackButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BackButton(Activity ctx, int previousMenuId) {
        this(ctx, previousMenuId, "menu.back");
    }

    public BackButton(Activity ctx, int previousMenuId, String unlocalized) {
        super(ctx, Localization.localize(unlocalized), () -> {
            ((MainActivity) ctx).setGameClient(null);
            ViewManager.show(ctx, previousMenuId);
        });

        setId(R.id.back_button);
    }

}
