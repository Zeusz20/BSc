package com.zeusz.bsc.editor.gui.workspace;

import com.zeusz.bsc.core.Attribute;
import com.zeusz.bsc.core.Question;
import com.zeusz.bsc.editor.Editor;
import com.zeusz.bsc.editor.gui.Box;
import com.zeusz.bsc.editor.gui.IconButton;
import com.zeusz.bsc.editor.gui.Prompt;
import com.zeusz.bsc.editor.gui.workspace.form.*;
import com.zeusz.bsc.editor.localization.Localization;

import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.scene.layout.Priority;

import java.util.List;


public class QuestionPane extends Workspace<Question> {

    public QuestionPane(Question question) {
        super(question);
    }

    @Override
    public void draw() {
        super.draw();
        Box questionBox = initQuestionBox();
        root.getChildren().add(questionBox);
    }

    private Box initQuestionBox() {
        List<Attribute> attributes = Editor.getInstance().getProject().getItemList(Attribute.class);

        TextInput textInput = new TextInput(item.getText());
        DropDownList<Attribute> attrsDropDown = new DropDownList<>(item.getAttribute(), attributes);

        Row.setHgrow(attrsDropDown, Priority.ALWAYS);

        fields.add(textInput);
        fields.add(attrsDropDown);

        return new Box(
                /* IconButton acts like margin, so text fields are lined up */
                new Row(new Prompt(Localization.prompt("word.question")), textInput, new IconButton(null)),
                new Separator(Orientation.HORIZONTAL),
                new Row(new Prompt(Localization.prompt("word.attribute")), attrsDropDown)
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public void save() {
        super.save();

        TextInput text = (TextInput) fields.get(1);
        DropDownList<Attribute> attribute = (DropDownList<Attribute>) fields.get(2);

        item.setText(text.getText());
        item.setAttribute(attribute.getValue());
    }

}
