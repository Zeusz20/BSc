package com.zeusz.bsc.app.menu;

import android.app.Activity;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.zeusz.bsc.app.widget.JSWebView;
import com.zeusz.bsc.core.Cloud;


public class DownloadMenu extends Menu {

    public DownloadMenu(Activity ctx, LinearLayout header, LinearLayout body, LinearLayout footer) {
        super(ctx, header, body, footer);
    }

    @Override
    protected void buildHeader(Activity ctx, LinearLayout header) {
        // no header
    }

    @Override
    protected void buildBody(Activity ctx, LinearLayout body) {
        JSWebView browser = new JSWebView(ctx);
        browser.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        browser.loadUrl(Cloud.getCloudUrl("/user/search/"));

        body.addView(browser);
    }

    @Override
    protected void buildFooter(Activity ctx, LinearLayout footer) {
        footer.addView(MenuBuilder.getBackButton(ctx, MainMenu.class));
    }

}
