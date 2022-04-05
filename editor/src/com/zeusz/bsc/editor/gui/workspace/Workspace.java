package com.zeusz.bsc.editor.gui.workspace;

import com.zeusz.bsc.core.*;
import com.zeusz.bsc.core.Object;
import com.zeusz.bsc.editor.gui.Box;
import com.zeusz.bsc.editor.gui.Prompt;
import com.zeusz.bsc.editor.gui.Scrollable;
import com.zeusz.bsc.editor.gui.explorer.SideMenu;
import com.zeusz.bsc.editor.gui.workspace.form.*;
import com.zeusz.bsc.editor.validation.ValidationHint;
import com.zeusz.bsc.editor.gui.Drawable;
import com.zeusz.bsc.editor.validation.ValidationIcon;
import com.zeusz.bsc.editor.localization.Localization;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

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
        root.setPadding(new Insets(20.0));
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

        title.setFont(Font.font(22.0));
        title.setPadding(new Insets(0.0, 0.0, 5.0, 0.0));

        return title;
    }

    protected Box initNameBox() {
        initValidationIcon(item);
        TextInput nameInput = new TextInput(item.getName());

        nameInput.textProperty().addListener((observable, oldValue, newValue) -> {
            // name cannot be empty
            if(!newValue.matches("\\s*"))
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
        if(name.getText().matches("\\s*"))
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
