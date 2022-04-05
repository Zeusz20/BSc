package com.zeusz.bsc.editor.gui.workspace.form;

import com.zeusz.bsc.editor.Editor;

import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;


/**
 * Every class that implements this interface
 * should call {@link ItemChangeListener#bindListener()} in it's constructor.
 * */
public interface ItemChangeListener {

    /**
     * @return The property or list to bind the listener to.
     * Should be a subclass of {@link ObservableValue} or {@link ObservableList} respectively.
     * */
    Observable getProperty();

    @SuppressWarnings({"rawtypes", "unchecked"})
    default void bindListener() {
        Observable property = getProperty();

        if(property instanceof ObservableValue) {
            ((ObservableValue) property).addListener((observable, oldValue, newValue) -> {
                Editor.getInstance().validateProject();
            });
        }
        else if(property instanceof ObservableList) {
            ((ObservableList) property).addListener((ListChangeListener) listener -> {
                Editor.getInstance().validateProject();
            });
        }
    }

}
