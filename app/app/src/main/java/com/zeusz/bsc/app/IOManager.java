package com.zeusz.bsc.app;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.widget.Toast;

import com.zeusz.bsc.core.Localization;

import java.io.File;
import java.util.Arrays;


public final class IOManager {

    private IOManager() { }

    private static BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, Localization.localize("download.complete"), Toast.LENGTH_SHORT).show();
        }
    };

    public static void download(Activity ctx, String url) {
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

    public static boolean fileExists(Activity ctx, String filename) {
        File[] contents = ctx.getExternalFilesDir(null).listFiles();

        if(contents != null)
            return Arrays.stream(contents).anyMatch(it -> it.getName().equals(filename));

        return true;
    }

}
