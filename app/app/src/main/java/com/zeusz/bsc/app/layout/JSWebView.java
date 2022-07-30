package com.zeusz.bsc.app.layout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import com.zeusz.bsc.app.IOManager;
import com.zeusz.bsc.core.Cloud;


public class JSWebView extends WebView {

    @SuppressLint("SetJavaScriptEnabled")
    public JSWebView(Activity ctx, @Nullable String homepage) {
        super(ctx);

        getSettings().setJavaScriptEnabled(true);
        setWebViewClient(new WebViewClient());

        setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            if(url.endsWith(".gwp")) IOManager.download(ctx, url);
            else loadUrl(url);
        });

        if(homepage != null)
            loadUrl(Cloud.getCloudUrl(homepage));
    }

}
