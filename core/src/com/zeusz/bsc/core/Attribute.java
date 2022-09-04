package com.zeusz.bsc.core;

import java.util.ArrayList;
import java.util.List;


public class Attribute extends Item {

    private static final long serialVersionUID = 4047167163L;

    private String question;
    private List<String> values;

    public Attribute(String name) {
        super(name);
        question = "";
        values = new ArrayList<>();
        values.add("");
    }

    public List<String> getValues() { return values; }

    public String getQuestion() { return question; }

    public void setQuestion(String question) { this.question = question; }

}
