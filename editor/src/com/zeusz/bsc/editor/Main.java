package com.zeusz.bsc.editor;

import com.zeusz.bsc.core.Localization;
import com.zeusz.bsc.editor.event.SaveWarningEvent;
import com.zeusz.bsc.editor.gui.window.SettingsWindow;
import com.zeusz.bsc.editor.io.ResourceLoader;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;


public final class Main extends Application {

    // TODO: write help

    public static void main(String[] args) {
        Application.launch(args);
        System.exit(0);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Localization.init();

        // set icon and title
        primaryStage.getIcons().add(ResourceLoader.getFXImage("img/icon.png"));
        primaryStage.setTitle(Localization.localize("window.title"));

        // set size (16:10 ratio)
        primaryStage.setMaximized(true);
        primaryStage.setMinWidth(960.0);
        primaryStage.setMinHeight(600.0);

        // init editor
        final Editor editor = Editor.getInstance();
        editor.setWindow(primaryStage);

        // display scene
        primaryStage.setScene(new Scene(editor.getRoot()));
        primaryStage.show();
        new SettingsWindow(editor.getProject()).show();

        // ask to save before closing
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            new SaveWarningEvent(Platform::exit).fire();
        });
    }

}
