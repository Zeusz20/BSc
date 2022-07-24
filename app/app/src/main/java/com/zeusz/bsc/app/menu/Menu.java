package com.zeusz.bsc.app.menu;

import android.app.Activity;
import android.widget.LinearLayout;


public abstract class Menu {

    public Menu(Activity ctx, LinearLayout header, LinearLayout body, LinearLayout footer) {
        buildHeader(ctx, header);
        buildBody(ctx, body);
        buildFooter(ctx, footer);
    }

    protected abstract void buildHeader(Activity ctx, LinearLayout header);
    protected abstract void buildBody(Activity ctx, LinearLayout body);
    protected abstract void buildFooter(Activity ctx, LinearLayout footer);

}
