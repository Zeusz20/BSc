package com.zeusz.bsc.app.layout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.zeusz.bsc.app.util.IOManager;
import com.zeusz.bsc.core.Cloud;
import com.zeusz.bsc.core.Localization;


public class JSWebView extends WebView {

    /* Preload JSWebView */
    private static JSWebView webView;

    public static void load(Activity ctx) {
        // load browser
        webView = new JSWebView(ctx, "/user/search/?lang=" + Localization.getLocale().getLanguage());
        webView.setLayoutParams(
            new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT)
        );
    }

    public static JSWebView getWebView() { return webView; }

    /* Class fields and methods */
    public JSWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public JSWebView(Activity ctx, @Nullable String homepage) {
        this(ctx, (AttributeSet) null);

        getSettings().setJavaScriptEnabled(true);
        setWebViewClient(new WebViewClient());

        setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            if(url.endsWith(".gwp"))
                IOManager.download(ctx, url);
            else
                loadUrl(url);
        });

        if(homepage != null)
            loadUrl(Cloud.getCloudUrl(homepage));
    }

}
