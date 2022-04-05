package com.zeusz.bsc.editor.gui.workspace.form;

import com.zeusz.bsc.editor.gui.FastTooltip;
import com.zeusz.bsc.editor.io.ResourceLoader;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;


public class AddButton extends Button {

    public AddButton() {
        setGraphic(new ImageView(ResourceLoader.getFXImage("img/add.png")));
        setShape(new Rectangle(24.0, 24.0));
    }

    public void setTooltip(String tooltip) {
        setTooltip(new FastTooltip(tooltip));
    }

}
