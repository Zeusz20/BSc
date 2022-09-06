package com.zeusz.bsc.app.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import com.zeusz.bsc.app.R;
import com.zeusz.bsc.app.dialog.ConcedeDialog;
import com.zeusz.bsc.core.Localization;


public class ConcedeButton extends MenuButton {

    public ConcedeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ConcedeButton(Activity ctx) {
        super(ctx, Localization.localize("game.concede"), () -> new ConcedeDialog(ctx).show());
        setId(R.id.back_button);
    }

}
