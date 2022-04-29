package com.zeusz.bsc.editor.gui.window;

import com.zeusz.bsc.core.Project;
import com.zeusz.bsc.editor.gui.Box;
import com.zeusz.bsc.editor.gui.Prompt;
import com.zeusz.bsc.editor.gui.workspace.form.Row;
import com.zeusz.bsc.editor.localization.Localization;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;


public class SettingsWindow extends Modal {

    private TextField author, name;
    private TextArea description;

    public SettingsWindow(Project project) {
        author = new TextField(project.getAuthor());
        name = new TextField(project.getName());
        description = new TextArea(project.getDescription());

        Row.setHgrow(author, Priority.ALWAYS);
        Row.setHgrow(name, Priority.ALWAYS);
        Row.setHgrow(description, Priority.ALWAYS);

        setOnClose(() -> {
            if(state == State.APPLY)
                project.setMeta(author.getText(), name.getText(), description.getText());
        });

        setTitle(Localization.localize("menu.settings"));
        init(Size.MEDIUM);

        author.requestFocus();
    }

    @Override
    protected Content getContent() {
        Box form = new Box(
                new Row(new Prompt(Localization.prompt("word.author")), author),
                new Row(new Prompt(Localization.prompt("word.name")), name),
                new Row(new Prompt(Localization.prompt("word.description")), description)
        );

        return new Content(form, okBtn, cancelBtn);
    }

}
