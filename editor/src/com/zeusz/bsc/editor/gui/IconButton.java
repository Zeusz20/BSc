package com.zeusz.bsc.editor.gui;

import com.zeusz.bsc.editor.event.MenuEvents;
import com.zeusz.bsc.editor.io.ResourceLoader;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;


public class IconButton extends Button {

    public IconButton(String path) {
        this(path, null, null, null);
    }

    public IconButton(String path, String name, String accelerator, String event) {
        getStylesheets().add("css/button.css");
        setFocusTraversable(false);
        setPadding(new Insets(0.0));
        setPrefSize(24.0, 24.0);

        setGraphic(new ImageView(ResourceLoader.getFXImage(path)));
        setOnAction(MenuEvents.getEventByName(event));

        if(name != null) {
            String tooltip = (accelerator == null) ? name : name + "\t(" + accelerator + ")";
            setTooltip(new FastTooltip(tooltip));
        }
    }

}
