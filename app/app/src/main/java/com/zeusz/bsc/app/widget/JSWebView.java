package com.zeusz.bsc.app.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.zeusz.bsc.app.R;

import java.io.File;
import java.util.Arrays;


public class JSWebView extends WebView {

    private static BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Download complete", Toast.LENGTH_SHORT).show();
        }
    };

    @SuppressLint("SetJavaScriptEnabled")
    public JSWebView(Activity ctx) {
        super(ctx);

        getSettings().setJavaScriptEnabled(true);
        setWebViewClient(new WebViewClient());

        setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            if(url.endsWith(".gwp"))
                downloadFile(ctx, url);
            else
                loadUrl(url);
        });
    }

    private void downloadFile(Activity ctx, String url) {
        String filename = url.substring(url.lastIndexOf('/') + 1);

        // Check if file is already downloaded. If not, queue a download request.
        if(!fileExists(ctx, filename)) {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setTitle(ctx.getResources().getString(R.string.app_name));
            request.setDestinationInExternalFilesDir(ctx, "projects", filename);

            DownloadManager manager = (DownloadManager) ctx.getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);

            // show toast message
            ctx.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }
        else {
            // show toast message anyway, so users won't get confused
            receiver.onReceive(ctx, null);
        }
    }

    private boolean fileExists(Activity ctx, String name) {
        File[] contents = ctx.getExternalFilesDir(null).listFiles();
        if(contents != null)
            return Arrays.stream(contents).anyMatch(it -> it.getName().equals(name));

        return true;
    }

}
