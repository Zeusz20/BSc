package com.zeusz.bsc.core;

import java.io.File;

import java.util.ArrayList;
import java.util.List;


public class Project extends GWObject {

    private static final long serialVersionUID = 2216838845L;

    private File source;
    private String author, name, description;
    public boolean valid;   // used at uploads on the server side

    private List<Object> objects;
    private List<Attribute> attributes;
    private List<Question> questions;

    public Project(String name) {
        this.source = null;
        this.author = "";
        this.name = name;
        this.description = "";
        this.valid = false;

        objects = new ArrayList<>();
        attributes = new ArrayList<>();
        questions = new ArrayList<>();
    }

    /* With enum I had to use raw types which was ugly. */
    public <T extends Item> List<T> getItemList(Class<T> type) {
        if(type == Object.class) return (ArrayList<T>) objects;
        if(type == Attribute.class) return (ArrayList<T>) attributes;
        if(type == Question.class) return (ArrayList<T>) questions;
        return new ArrayList<>(0);
    }

    public void setMeta(String author, String name, String description) {
        this.author = author;
        this.name = name;
        this.description = description;
    }

    public void setSource(File source) { this.source = source; }

    public File getSource() { return source; }

    public String getAuthor() { return author; }

    public String getName() { return name; }

    public String getDescription() { return description; }

}
