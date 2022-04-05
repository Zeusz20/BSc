package com.zeusz.bsc.editor.io;

import com.zeusz.bsc.core.ImageCache;

import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;


public final class FXImageCache extends ImageCache<Image> {

    /* Singleton */
    private static FXImageCache INSTANCE = new FXImageCache();
    public static FXImageCache getInstance() { return INSTANCE; }

    @Override
    protected Image getImage(byte[] bytes) {
        if(bytes == null) return null;
        return new Image(new ByteArrayInputStream(bytes));
    }

}
