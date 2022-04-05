package com.zeusz.bsc.editor.gui.workspace.form;

import com.zeusz.bsc.editor.gui.Box;

import javafx.beans.Observable;

import java.util.List;
import java.util.stream.Collectors;


public class ItemList extends Box implements ItemChangeListener {

    public ItemList() {
        bindListener();
    }

    public void addRow(Row row) {
        try {
            getChildren().add(row);
        }
        catch(IllegalStateException e) {
            // row wasn't valid (see AttributeRow's constructor)
        }
    }

    public List<Row> getRows() {
        // cast Node as Row
        return getChildren().stream().map(it -> (Row) it).collect(Collectors.toList());
    }

    @Override
    public Observable getProperty() {
        return getChildren();
    }

}
