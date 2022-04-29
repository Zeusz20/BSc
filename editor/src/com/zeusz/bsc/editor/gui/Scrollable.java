package com.zeusz.bsc.editor.gui;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;


public class Scrollable extends ScrollPane {

    public Scrollable(Node content) {
        super(content);
        setPadding(Style.PADDING_NONE);
        setFitToWidth(true);
        setFitToHeight(true);
        setStyle(Style.BG_TRANSPARENT);
    }

}
