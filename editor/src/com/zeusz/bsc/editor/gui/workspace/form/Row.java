package com.zeusz.bsc.editor.gui.workspace.form;

import com.zeusz.bsc.core.Item;
import com.zeusz.bsc.editor.gui.Style;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;


public class Row extends HBox {

    public static HBox getFiller() {
        HBox filler = new HBox();
        setHgrow(filler, Priority.ALWAYS);
        return filler;
    }

    private Item item;  // can store Item on FXParent

    public Row(Node... children) {
        setSpacing(Style.SPACING_MEDIUM);
        init(children);
    }

    protected void init(Node... children) {
        for(Node child : children)
            if(child != null)
                getChildren().add(child);
    }

    public void setItem(Item item) { this.item = item; }

    public Item getItem() { return item; }

    /**
     * Finds the first node with the given class.
     * @param target Node's class.
     * @return First matching node.
     * */
    public Node getControl(Class<? extends Node> target) {
        return getChildren().stream().filter(it -> it.getClass().equals(target)).findFirst().orElse(null);
    }

}
