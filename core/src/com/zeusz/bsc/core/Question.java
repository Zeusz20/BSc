package com.zeusz.bsc.core;


public class Question extends Item {

    private static final long serialVersionUID = 911647670L;

    private String text;
    private Attribute attribute;

    public Question(String name) {
        super(name);
        text = "";
        attribute = null;
    }

    public void setText(String text) { this.text = text; }

    public String getText() { return text; }

    public void setAttribute(Attribute attribute) { this.attribute = attribute; }

    public Attribute getAttribute() { return attribute; }

}
