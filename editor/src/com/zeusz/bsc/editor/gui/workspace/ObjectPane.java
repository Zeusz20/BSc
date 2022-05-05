package com.zeusz.bsc.editor.gui.workspace;

import com.zeusz.bsc.core.Attribute;
import com.zeusz.bsc.core.Localization;
import com.zeusz.bsc.core.Object;
import com.zeusz.bsc.core.Pair;
import com.zeusz.bsc.editor.gui.Box;
import com.zeusz.bsc.editor.gui.IconButton;
import com.zeusz.bsc.editor.gui.Style;
import com.zeusz.bsc.editor.gui.workspace.form.*;
import com.zeusz.bsc.editor.gui.window.AttributeWindow;
import com.zeusz.bsc.editor.gui.window.Modal;

import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;

import java.util.List;
import java.util.stream.Collectors;


public class ObjectPane extends Workspace<Object> {

    public ObjectPane(Object object) {
        super(object);
    }

    @Override
    public void draw() {
        super.draw();
        Box imageBox = initImageBox();
        Box attributeBox = initAttributeBox();
        root.getChildren().addAll(imageBox, new Separator(Orientation.HORIZONTAL), attributeBox);
    }

    private Box initImageBox() {
        ImageBox imageBox = new ImageBox(item);
        fields.add(imageBox.getImagePreview());
        return imageBox;
    }

    private Box initAttributeBox() {
        Label label = new Label(Localization.localize("form.add_attribute"));
        IconButton addBtn = new IconButton("img/add.png");
        ItemList attributeList = new ItemList();

        addBtn.setOnAction(event -> {
            AttributeWindow window = new AttributeWindow();
            window.show();
            window.setOnClose(() -> {
                if(window.getState() == Modal.State.APPLY) {
                    Attribute attribute = window.getSelected();
                    if(attribute != null) {
                        Pair<Attribute, String> pair = new Pair<>(attribute, attribute.getValues().get(0));
                        attributeList.addRow(new AttributeRow(attributeList, pair));
                    }
                }
            });
        });

        label.setFont(Style.FONT_SMALL);
        item.getAttributes().forEach(it -> attributeList.addRow(new AttributeRow(attributeList, it)));  // populate attributeList
        fields.add(attributeList);

        return new Box(new Row(label, addBtn), attributeList);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void save() {
        super.save();

        ImagePreview image = (ImagePreview) fields.get(1);
        ItemList attributeList = (ItemList) fields.get(2);

        // save image
        item.setImage(image.getImageBuffer());

        // map attribute -> value (String) pairs
        List<Pair<Attribute, String>> attrValuePairs = attributeList.getRows().stream().map(it -> {
            Attribute attribute = (Attribute) it.getItem();
            DropDownList<String> values = (DropDownList<String>) it.getControl(DropDownList.class);
            return new Pair<>(attribute, values.getValue());
        }).collect(Collectors.toList());

        item.getAttributes().clear();
        item.getAttributes().addAll(attrValuePairs);
    }

}
