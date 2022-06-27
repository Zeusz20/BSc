package com.zeusz.bsc.app.menu;

import android.app.Activity;
import androidx.appcompat.widget.AppCompatButton;


public class MenuButton extends AppCompatButton {

    public MenuButton(Activity ctx, String text, int eventID) {
        super(ctx);
        setText(text);
        setOnClickListener(MenuEvents.getEvent(ctx, eventID));
    }

}
