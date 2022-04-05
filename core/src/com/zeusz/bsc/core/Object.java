package com.zeusz.bsc.core;

import java.util.ArrayList;
import java.util.List;


public class Object extends Item {

    private static final long serialVersionUID = 931422573L;

    private byte[] image;
    private List<Pair<Attribute, String>> attributes;

    public Object(String name) {
        super(name);
        image = null;
        attributes = new ArrayList<>();
    }

    public void setImage(byte[] image) { this.image = image; }

    public byte[] getImage() { return image; }

    public List<Pair<Attribute, String>> getAttributes() { return attributes; }

}
