package com.zeusz.bsc.editor.gui.workspace;

import com.zeusz.bsc.core.*;
import com.zeusz.bsc.core.Object;
import com.zeusz.bsc.editor.gui.*;
import com.zeusz.bsc.editor.gui.explorer.SideMenu;
import com.zeusz.bsc.editor.gui.workspace.form.*;
import com.zeusz.bsc.editor.validation.Validation;
import com.zeusz.bsc.editor.validation.ValidationHint;
import com.zeusz.bsc.editor.validation.ValidationIcon;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
import java.util.List;


public abstract class Workspace<T extends Item> extends BorderPane implements Drawable, ValidationHint {

    // Workspace factory
    public static Workspace<?> getWorkspace(Item item) {
        if(item.getClass() == Object.class)
            return new ObjectPane((Object) item);

        if(item.getClass() == Attribute.class)
            return new AttributePane((Attribute) item);

        if(item.getClass() == Question.class)
            return new QuestionPane((Question) item);

        return null;
    }

    /* Class methods and fields */
    protected T item;
    protected List<Node> fields;  // Item fields (name, images, attributes, etc.);  used for SAVING
    protected ValidationIcon icon;

    protected Box root;

    protected Workspace(T item) {
        this.item = item;
        this.fields = new ArrayList<>();

        root = new Box();
        root.setPadding(Style.PADDING_LARGE);
        root.setFillWidth(true);

        draw();     // render fx children

        setCenter(new Scrollable(root));
        setRight(HelpPane.getHelpFor(item.getClass()));
        fields.get(0).requestFocus();
    }

    private Label initTitle() {
        String type = item.getClass().getSimpleName().toLowerCase();
        String text = Localization.capLocalize("word." + type) + " " + Localization.localize("word.editor");
        Label title = new Label(text);

        title.setFont(Style.FONT_LARGE);
        title.setPadding(Style.PADDING_BOTTOM);

        return title;
    }

    protected Box initNameBox() {
        initValidationIcon(item);
        TextInput nameInput = new TextInput(item.getName());

        nameInput.textProperty().addListener((observable, oldValue, newValue) -> {
            // name cannot be empty
            if(!newValue.matches(Validation.EMPTY_PATTERN))
                SideMenu.getActiveLabel().setText(newValue);
        });

        fields.add(nameInput);

        return new Box(new Row(new Prompt(Localization.prompt("word.name")), nameInput, icon));
    }

    @Override
    public void draw() {
        Label title = initTitle();
        Box nameBox = initNameBox();

        root.getChildren().addAll(title, nameBox);
    }

    public void save() {
        TextInput name = (TextInput) fields.get(0);

        // name cannot be empty
        if(name.getText().matches(Validation.EMPTY_PATTERN))
            item.setName(SideMenu.getActiveLabel().getText());
        else
            item.setName(name.getText());
    }

    public Item getItem() { return item; }

    @Override
    public void initValidationIcon(GWObject object) {
        icon = new ValidationIcon(object);
    }

    @Override
    public ValidationIcon getValidationIcon() {
        return icon;
    }

}
