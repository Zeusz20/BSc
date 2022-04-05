package com.zeusz.bsc.editor.gui;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;


public class Scrollable extends ScrollPane {

    public Scrollable(Node content) {
        super(content);
        setPadding(new Insets(0.0));
        setFitToWidth(true);
        setFitToHeight(true);
        setStyle("-fx-background-color: transparent");
    }

}
