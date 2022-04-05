package com.zeusz.bsc.editor.gui.explorer;

import com.zeusz.bsc.core.Item;
import com.zeusz.bsc.editor.Editor;
import com.zeusz.bsc.editor.gui.workspace.Workspace;
import com.zeusz.bsc.editor.gui.IconButton;

import javafx.scene.control.Hyperlink;
import javafx.scene.layout.BorderPane;


public class ItemLabel extends BorderPane {

    public ItemLabel(Item item) {
        Hyperlink link = new Hyperlink(item.getName());
        IconButton deleteBtn = new IconButton("img/del.png");

        link.setMaxWidth(150.0);
        link.setPrefWidth(150.0);
        link.getStylesheets().add("css/itemlabel.css");
        link.setOnMouseEntered(event -> setBorderColor("turquoise"));
        link.setOnMouseExited(event -> setBorderColor("transparent"));
        link.setOnAction(event -> {
            Editor.getInstance().initWorkspace(Workspace.getWorkspace(item));
            SideMenu.setActiveLabel(this);
        });

        deleteBtn.setOnAction(event -> {
            /* FOR NOW !!!
             * To "fix" the retroactive attribute delete bug when workspace is initialized with Object or Question,
             * is to delete the whole workspace. */
            Editor.getInstance().initWorkspace(null);

            // release locked label
            if(SideMenu.getActiveLabel() != null)
                SideMenu.getActiveLabel().setDisable(false);

            Editor.getInstance().removeItem(item);
        });

        setBorderColor("transparent");
        setLeft(link);
        setRight(deleteBtn);
    }

    private void setBorderColor(String color) {
        setStyle("-fx-border-color: " + color);
    }

}
