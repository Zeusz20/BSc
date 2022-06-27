package com.zeusz.bsc.app.menu;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zeusz.bsc.app.JSWebView;
import com.zeusz.bsc.app.R;
import com.zeusz.bsc.core.Cloud;


public final class MenuEvents {

    public static final int NEW_GAME = 0;
    public static final int EXIT_GAME = 1;
    public static final int DOWNLOAD = 2;

    public static View.OnClickListener getEvent(Activity ctx, int eventID) {
        switch(eventID) {
            case NEW_GAME:
                return null;

            case EXIT_GAME:
                return listener -> ctx.finish();

            case DOWNLOAD:
                return listener -> {
                    ctx.setContentView(R.layout.download_screen);
                    JSWebView browser = ctx.findViewById(R.id.browser);
                    browser.init(ctx);  // set up download procedure
                    browser.loadUrl(Cloud.getCloudUrl("/user/search/"));
                };
        }

        return null;
    }

}
