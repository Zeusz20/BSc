package com.zeusz.bsc.editor.io;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;


public final class ResourceLoader {

    private ResourceLoader() { }

    public static URL getResource(String path) {
        return (path != null) ? ResourceLoader.class.getClassLoader().getResource(path) : null;
    }

    public static File getFile(String path) {
        if(path == null) return null;

        try {
            URL resource = getResource(path);
            return (resource != null) ? new File(resource.toURI()) : new File(path);
        }
        catch(URISyntaxException e) {
            return null;
        }
    }

    public static Image getFXImage(String path) {
        if(path == null) return null;

        try {
            File file = ResourceLoader.getFile(path);
            if(file != null) return new Image(new FileInputStream(file));
        }
        catch(IOException e) {
            // couldn't read file
        }

        return null;
    }

}
