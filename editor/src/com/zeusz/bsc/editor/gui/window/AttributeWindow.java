package com.zeusz.bsc.editor.gui.window;

import com.zeusz.bsc.core.Attribute;
import com.zeusz.bsc.editor.Editor;
import com.zeusz.bsc.editor.localization.Localization;

import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;


public class AttributeWindow extends Modal {

    private ComboBox<Attribute> attributes;

    public AttributeWindow() {
        attributes = new ComboBox<>();
        attributes.getItems().addAll(Editor.getInstance().getProject().getItemList(Attribute.class));
        attributes.setPromptText(Localization.localize("window.choose_attr"));
        attributes.setPrefWidth(Size.SMALL.width);    // HBox::setHgrow doesn't work

        init(Size.SMALL);
    }

    public Attribute getSelected() { return attributes.getValue(); }

    @Override
    protected Content getContent() {
        return new Content(new HBox(attributes), okBtn, cancelBtn);
    }

}
