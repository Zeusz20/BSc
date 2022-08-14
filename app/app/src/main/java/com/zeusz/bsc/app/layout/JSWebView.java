package com.zeusz.bsc.app.layout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import com.zeusz.bsc.app.io.IOManager;
import com.zeusz.bsc.core.Cloud;


public class JSWebView extends WebView {

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
