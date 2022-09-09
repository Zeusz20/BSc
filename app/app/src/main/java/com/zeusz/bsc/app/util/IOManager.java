package com.zeusz.bsc.app.util;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import com.zeusz.bsc.app.R;
import com.zeusz.bsc.app.network.DownloadReceiver;
import com.zeusz.bsc.core.Project;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public final class IOManager {

    /* FileWriter singleton */
    private static final FileWriter FILE_WRITER = new FileWriter();
    public static FileWriter getFileWriter() { return FILE_WRITER; }

    /* IOManager */
    private IOManager() { }

    public static final String PROJECT_DIR = "projects";

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

    public static byte[] getFileBytes(File file) {
        try(FileInputStream stream = new FileInputStream(file)) {
            byte[] bytes = new byte[stream.available()];

            stream.read(bytes);
            return bytes;
        }
        catch(IOException e) {
            return new byte[0];
        }
    }

    public static void moveProjects(Activity ctx) throws NullPointerException {
        for(File file: Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).listFiles()) {
            if(file.getName().endsWith(".gwp"))
                file.renameTo(new File(ctx.getExternalFilesDir(PROJECT_DIR), file.getName()));
        }
    }

    public static List<File> listProjects(Activity ctx) {
        File[] files = ctx.getExternalFilesDir(PROJECT_DIR).listFiles();
        return (files != null) ? new ArrayList<>(Arrays.asList(files)) : new ArrayList<>();
        // Arrays::asList and Collections::emptyList() return readonly lists
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

    /** @return A string representation of a byte array. */
    public static String bytesToString(byte[] bytes) {
        StringBuilder builder = new StringBuilder();

        for(byte b: bytes)
            builder.append(b).append(',');

        return builder.toString();
    }

    /** @return Converts a string representation back to a byte array. */
    public static byte[] stringToBytes(String string) {
        List<String> values = Arrays.stream(string.split(",")).filter(it -> !it.equals("")).collect(Collectors.toList());
        byte[] bytes = new byte[values.size()];

        for(int i = 0; i < values.size(); i++)
            bytes[i] = Byte.parseByte(values.get(i));

        return bytes;
    }

    /**
     * Singleton wrapper class for FileOutputStream.
     * Used when transferring files between devices.
     *  */
    public static final class FileWriter implements Closeable {
        private File file;
        private FileOutputStream stream;
        private boolean open;
        private StringBuilder buffer;

        private FileWriter() { open = true; }

        public void init(Activity ctx, String filename) throws IOException {
            String dir = ctx.getExternalFilesDir(IOManager.PROJECT_DIR).getAbsolutePath();

            file = new File(dir, filename);
            if(file.exists()) file.delete();

            if(stream != null) stream.close();
            stream = new FileOutputStream(file);
            buffer = new StringBuilder();
            open = true;
        }

        public void write(String bytes) {
            buffer.append(bytes);
        }

        @Override
        public void close() throws IOException {
            open = false;

            // remove trailing white space
            buffer.deleteCharAt(buffer.length() - 1);

            if(stream != null) {
                stream.write(IOManager.stringToBytes(buffer.toString()));
                stream.close();
                stream = null;
            }
        }

        public boolean isOpen() { return open; }
        public File getFile() { return file; }
    }

}
