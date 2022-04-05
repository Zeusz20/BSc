package com.zeusz.bsc.core;

import java.util.HashMap;
import java.util.Map;


/** @param <I> represents the class of the image which is used to RENDER it in the given environment. */
public abstract class ImageCache<I> {

    protected Map<Integer, I> imageCache;

    protected ImageCache() {
        imageCache = new HashMap<>();
    }

    public void cacheImage(Object object, I image) {
        imageCache.put(object.hashCode(), image);
    }

    public void disposeImage(Object object) {
        imageCache.remove(object.hashCode());
    }

    public I getImage(Object object) {
        I image = imageCache.get(object.hashCode());

        if(image == null) {
            image = getImage(object.getImage());
            cacheImage(object, image);
        }

        return image;
    }

    protected abstract I getImage(byte[] image);

}
