package com.zeusz.bsc.app.layout;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.zeusz.bsc.app.R;
import com.zeusz.bsc.app.network.GameClient;
import com.zeusz.bsc.app.widget.Label;
import com.zeusz.bsc.app.widget.MenuButton;
import com.zeusz.bsc.app.widget.TextInput;
import com.zeusz.bsc.core.Localization;


public class JoinGameForm extends MenuLayout {

    public JoinGameForm(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JoinGameForm(Activity ctx) {
        super(ctx, VERTICAL);

        Label label = new Label(ctx, Localization.localize("menu.enter_id"));
        label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18.0f);

        addView(label);
        addView(new TextInput(ctx));
        addView(new MenuButton(ctx, Localization.localize("menu.connect"), () -> {
            Editable raw = ((TextInput) ctx.findViewById(R.id.text_input)).getText();

            if(raw != null) {
                String id = raw.toString().toUpperCase();
                GameClient.joinGame(ctx, id);
            }
        }));
    }

}
