package com.zeusz.bsc.editor.gui;

import javafx.scene.Node;
import javafx.scene.layout.VBox;


public class Box extends VBox {

    public Box(Node... children) {
        super(children);
        setSpacing(Style.SPACING_LARGE);
    }

}
