package com.zeusz.bsc.editor.gui.workspace.form;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;


public class TextInput extends TextField implements ItemChangeListener {

    public TextInput(String value) {
        Row.setHgrow(this, Priority.ALWAYS);
        setText(value);
        bindListener();
    }

    @Override
    public void requestFocus() {
        Platform.runLater(super::requestFocus);
    }

    @Override
    public Observable getProperty() {
        return textProperty();
    }

}
