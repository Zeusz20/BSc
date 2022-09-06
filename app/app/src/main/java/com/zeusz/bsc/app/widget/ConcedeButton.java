package com.zeusz.bsc.app.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import com.zeusz.bsc.app.R;
import com.zeusz.bsc.app.ui.DialogBuilder;
import com.zeusz.bsc.core.Localization;


public class ConcedeButton extends MenuButton {

    public ConcedeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ConcedeButton(Activity ctx) {
        super(ctx, Localization.localize("game.concede"), () -> DialogBuilder.concede(ctx));
        setId(R.id.back_button);
    }

}
