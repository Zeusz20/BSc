package com.zeusz.bsc.core;


public abstract class Item extends GWObject {

    private static final long serialVersionUID = 104834034L;

    protected String name;

    protected Item(String name) {
        this.name = name;
    }

    public final void setName(String name) { this.name = name; }

    public final String getName() { return name; }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(java.lang.Object obj) {
        return (obj instanceof Item) && ((Item) obj).getName().equals(name);
    }

}
