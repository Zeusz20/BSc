package com.zeusz.bsc.editor.gui.workspace.form;

import com.zeusz.bsc.core.Attribute;
import com.zeusz.bsc.core.Object;
import com.zeusz.bsc.core.Pair;
import com.zeusz.bsc.editor.Editor;
import com.zeusz.bsc.editor.gui.IconButton;
import com.zeusz.bsc.editor.gui.Prompt;
import javafx.scene.layout.Priority;


public class AttributeRow extends Row {

    public AttributeRow(ItemList parent, Pair<Attribute, String> pair) {
        if(pair == null) throw new IllegalStateException();

        Attribute attribute = pair.getKey();
        String value = pair.getValue();

        Prompt attrName = new Prompt(attribute.getName());
        DropDownList<String> values = new DropDownList<>(value, attribute.getValues());
        IconButton deleteAttrBtn = new IconButton("img/del.png");

        setHgrow(values, Priority.ALWAYS);

        deleteAttrBtn.setOnAction(event -> {
            Object item = (Object) Editor.getInstance().getWorkspace().getItem();  // active item
            item.getAttributes().remove(new Pair<>(attribute, value));
            parent.getChildren().remove(this);
        });

        super.init(attrName, values, deleteAttrBtn);
        setItem(attribute);
    }

}
