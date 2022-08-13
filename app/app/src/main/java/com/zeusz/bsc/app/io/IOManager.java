package com.zeusz.bsc.app.io;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.Toast;

import com.zeusz.bsc.app.R;
import com.zeusz.bsc.core.Localization;
import com.zeusz.bsc.core.Project;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Arrays;


public final class IOManager {

    private static final String PROJECT_DIR = "projects";

    private IOManager() { }

    private static BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, Localization.localize("browser.download_complete"), Toast.LENGTH_SHORT).show();
        }
    };

    public static void download(Activity ctx, String url) {
        String filename = url.substring(url.lastIndexOf('/') + 1);

        // Check if file is already downloaded. If not, queue a download request.
        if(!fileExists(ctx, filename)) {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setTitle(ctx.getResources().getString(R.string.app_name));
            request.setDestinationInExternalFilesDir(ctx, PROJECT_DIR, filename);

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
        // if IOManager::listProjects fails automatically assume file exists
        File[] projects = listProjects(ctx);
        return projects.length == 0 || Arrays.stream(projects).anyMatch(it -> it.getName().equals(filename));
    }

    public static File[] listProjects(Activity ctx) {
        File[] projects = ctx.getExternalFilesDir(PROJECT_DIR).listFiles();
        return (projects != null) ? projects : new File[0];
    }

    public static Project loadProjectByFilename(Activity ctx, String filename) {
        for(File file: listProjects(ctx))
            if(file.getName().equals(filename))
                return loadProject(file);

        return null;
    }

    public static Project loadProject(File file) {
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Project) ois.readObject();
        }
        catch(Exception e) {
            // couldn't read file
            return null;
        }
    }

    public static Bitmap getImage(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

}
