package com.zeusz.bsc.editor.io;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public final class ResourceLoader {

    private ResourceLoader() { }

    public static InputStream getFile(String path) {
        if(path == null) return null;

        try {
            InputStream resource = ResourceLoader.class.getClassLoader().getResourceAsStream(path);
            return (resource == null) ? new FileInputStream(path) : resource;
        }
        catch(IOException e) {
            return null;
        }
    }

    public static Image getFXImage(String path) {
        if(path == null) return null;

        InputStream file = ResourceLoader.getFile(path);
        if(file != null) return new Image(file);

        return null;
    }

}
