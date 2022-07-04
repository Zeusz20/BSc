package com.zeusz.bsc.app.menu;

import android.app.Activity;
import android.view.View;

import com.zeusz.bsc.app.JSWebView;
import com.zeusz.bsc.app.R;
import com.zeusz.bsc.core.Cloud;


public final class MenuEvents {

    public static final int NEW_GAME = 0;
    public static final int EXIT_GAME = 1;
    public static final int DOWNLOAD = 2;
    public static final int OPTIONS = 3;

    public static View.OnClickListener getEvent(Activity ctx, int eventID) {
        switch(eventID) {
            case NEW_GAME:
                return listener -> Menu.show(ctx, Menu.NEW_GAME_MENU);

            case EXIT_GAME:
                return listener -> ctx.finish();

            case DOWNLOAD:
                return listener -> {
                    ctx.setContentView(R.layout.download_screen);
                    JSWebView browser = ctx.findViewById(R.id.browser);
                    browser.init(ctx);
                    browser.loadUrl(Cloud.getCloudUrl("/user/search/"));
                };

            case OPTIONS:
                return listener -> Menu.show(ctx, Menu.OPTIONS_MENU);
        }

        return null;
    }

}
