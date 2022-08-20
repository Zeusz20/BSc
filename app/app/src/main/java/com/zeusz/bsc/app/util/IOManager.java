package com.zeusz.bsc.app.util;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.zeusz.bsc.app.R;
import com.zeusz.bsc.app.network.DownloadReceiver;
import com.zeusz.bsc.core.Project;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public final class IOManager {

    private static final String PROJECT_DIR = "projects";

    private IOManager() { }

    public static String encodeString(String string) {
        try { return URLEncoder.encode(string, StandardCharsets.UTF_8.name()); }
        catch(Exception e) { return string; }
    }

    public static String decodeString(String string) {
        try { return URLDecoder.decode(string, StandardCharsets.UTF_8.name()); }
        catch(Exception e) { return string; }
    }

    public static void download(Activity ctx, String url) {
        String filename = decodeString(url.substring(url.lastIndexOf('/') + 1));
        DownloadReceiver receiver = new DownloadReceiver(filename);

        // Check if file is already downloaded. If not, queue a download request.
        if(!fileExists(ctx, filename)) {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setTitle(ctx.getResources().getString(R.string.app_name));
            request.setDestinationInExternalFilesDir(ctx, PROJECT_DIR, filename);

            DownloadManager manager = (DownloadManager) ctx.getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);

            // show toast message (receiver will unregister itself)
            ctx.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }
        else {
            // show toast message anyway, so users won't get confused
            receiver.inform(ctx);
        }
    }

    public static boolean fileExists(Activity ctx, String filename) {
        return listProjects(ctx).stream().anyMatch(it -> it.getName().equals(filename));
    }

    public static List<File> listProjects(Activity ctx) {
        File[] files = ctx.getExternalFilesDir(PROJECT_DIR).listFiles();
        return (files != null) ? Arrays.asList(files) : Collections.emptyList();
    }

    public static Project loadProjectByFilename(Activity ctx, String filename) {
        for(File file: listProjects(ctx))
            if(file.getName().equals(filename))
                return loadProject(file);

        return null;
    }

    public static Project loadProject(File file) {
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Project project = (Project) ois.readObject();
            project.setSource(file);

            return project;
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
