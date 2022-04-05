package com.zeusz.bsc.editor.gui.workspace.form;

import javafx.beans.Observable;
import javafx.scene.control.ComboBox;

import java.util.List;


public class DropDownList<T> extends ComboBox<T> implements ItemChangeListener {

    public DropDownList(T selected, List<T> items) {
        getItems().addAll(items);
        getSelectionModel().select(selected);
        setMaxWidth(Double.POSITIVE_INFINITY);

        bindListener();
    }

    @Override
    public Observable getProperty() {
        return valueProperty();
    }

}
