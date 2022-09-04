package com.zeusz.bsc.editor.gui.workspace;

import com.zeusz.bsc.core.Attribute;
import com.zeusz.bsc.core.Localization;
import com.zeusz.bsc.editor.gui.Box;
import com.zeusz.bsc.editor.gui.IconButton;
import com.zeusz.bsc.editor.gui.Prompt;
import com.zeusz.bsc.editor.gui.Style;
import com.zeusz.bsc.editor.gui.workspace.form.*;

import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.Priority;

import java.util.List;
import java.util.stream.Collectors;


public class AttributePane extends Workspace<Attribute> {

    public AttributePane(Attribute attribute) {
        super(attribute);
    }

    @Override
    public void draw() {
        super.draw();
        Box questionBox = initQuestionBox();
        Box valueBox = initValueBox();
        root.getChildren().addAll(questionBox, new Separator(Orientation.HORIZONTAL), valueBox);
    }

    private Box initQuestionBox() {
        TextInput textInput = new TextInput(item.getQuestion());
        IconButton iconButton = new IconButton("img/del.png");

        fields.add(textInput);
        iconButton.setOnAction(click -> textInput.setText(""));

        return new Box(new Row(new Prompt(Localization.prompt("word.question")), textInput, iconButton));
    }

    private Box initValueBox() {
        Label label = new Label(Localization.localize("form.add_value"));
        IconButton addBtn = new IconButton("img/add.png");
        ItemList valueList = new ItemList();

        label.setFont(Style.FONT_SMALL);
        addBtn.setOnAction(event -> valueList.addRow(new ValueRow(valueList, "")));

        // populate valueList
        for(String value: item.getValues())
            valueList.addRow(new ValueRow(valueList, value));

        //item.getValues().forEach(it -> valueList.addRow(new ValueRow(valueList, it)));  // this throws a ConcurrentModificationException
        fields.add(valueList);

        return new Box(new Row(label, addBtn), valueList);
    }

    @Override
    public void save() {
        super.save();

        TextInput textInput = (TextInput) fields.get(1);
        ItemList valueList = (ItemList) fields.get(2);

        // save question text
        item.setQuestion(textInput.getText());

        // get values as String from valueList
        List<String> values = valueList.getRows().stream().map(it -> {
            TextInput value = (TextInput) it.getControl(TextInput.class);
            return value.getText();
        }).collect(Collectors.toList());

        item.getValues().clear();
        item.getValues().addAll(values);
    }

}
