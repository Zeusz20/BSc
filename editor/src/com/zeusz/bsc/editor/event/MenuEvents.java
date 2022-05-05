package com.zeusz.bsc.editor.event;

import com.zeusz.bsc.core.*;
import com.zeusz.bsc.core.Object;
import com.zeusz.bsc.editor.Editor;
import com.zeusz.bsc.editor.gui.window.Browser;
import com.zeusz.bsc.editor.gui.window.InfoWindow;
import com.zeusz.bsc.editor.gui.window.SettingsWindow;
import com.zeusz.bsc.editor.io.IOManager;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.lang.reflect.Field;


public final class MenuEvents {

    private MenuEvents() { }

    @SuppressWarnings("unchecked")
    public static EventHandler<ActionEvent> getEventByName(String name) {
        try {
            Field[] events = MenuEvents.class.getFields();
            for(Field field: events) {
                if(field.getName().equalsIgnoreCase(name))
                    return (EventHandler<ActionEvent>) field.get(null);
            }
        }
        catch(IllegalAccessException e) {
            // event is private
        }

        return null;
    }

    public static final EventHandler<ActionEvent> NEW = event -> {
        new SaveWarningEvent(() -> {
            Editor.getInstance().initProject(new Project(Localization.localize("word.project")));
            Editor.getInstance().validateProject(); // validate new project
            new SettingsWindow(Editor.getInstance().getProject()).show();
        }).fire();
    };

    public static final EventHandler<ActionEvent> OPEN = event -> {
        new SaveWarningEvent(() -> {
            if(IOManager.getInstance().load())
                Editor.getInstance().validateProject();  // validate opened project
            else
                new InfoWindow(Localization.localize("error.load")).show();
        }).fire();
    };

    public static final EventHandler<ActionEvent> SAVE = event -> {
        if(!IOManager.getInstance().save())
            new InfoWindow(Localization.localize("error.save")).show();
    };

    public static final EventHandler<ActionEvent> SAVE_AS = event -> {
        if(!IOManager.getInstance().saveAs())
            new InfoWindow(Localization.localize("error.save")).show();
    };

    public static final EventHandler<ActionEvent> SETTINGS = event -> {
        new SettingsWindow(Editor.getInstance().getProject()).show();
    };

    public static final EventHandler<ActionEvent> ADD_OBJECT = event -> {
        Editor.getInstance().addItem(new Object(Localization.localize("word.object")));
    };

    public static final EventHandler<ActionEvent> ADD_ATTRIBUTE = event -> {
        Editor.getInstance().addItem(new Attribute(Localization.localize("word.attribute")));
    };

    public static final EventHandler<ActionEvent> ADD_QUESTION = event -> {
        Editor.getInstance().addItem(new Question(Localization.localize("word.question")));
    };

    public static final EventHandler<ActionEvent> CLOUD = event -> {
        new Browser().show();
    };

}
