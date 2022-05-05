package com.zeusz.bsc.editor.gui.workspace.form;

import com.zeusz.bsc.core.Attribute;
import com.zeusz.bsc.core.Localization;
import com.zeusz.bsc.editor.Editor;
import com.zeusz.bsc.editor.event.ValueChangeEvent;
import com.zeusz.bsc.editor.event.ValueDeleteEvent;
import com.zeusz.bsc.editor.gui.IconButton;
import com.zeusz.bsc.editor.gui.Prompt;


public class ValueRow extends Row {

    public ValueRow(ItemList parent, String value) {
        Prompt prompt = new Prompt(Localization.prompt("word.value"));
        TextInput valueField = new TextInput(value);
        IconButton deleteValueBtn = new IconButton("img/del.png");

        valueField.textProperty().addListener((observable, oldValue, newValue) -> {
            Attribute attribute = (Attribute) Editor.getInstance().getWorkspace().getItem();  // active item
            new ValueChangeEvent(attribute, oldValue, newValue).fire();  // change values in objects
        });

        deleteValueBtn.setOnAction(event -> {
            // cannot delete last value
            if(parent.getRows().size() != 1) {
                parent.getChildren().remove(this);
                new ValueDeleteEvent(valueField.getText()).fire();
            }
        });

        super.init(prompt, valueField, deleteValueBtn);
    }

}
