package com.zeusz.bsc.editor.gui.window;

import com.zeusz.bsc.core.Cloud;
import com.zeusz.bsc.core.Localization;
import com.zeusz.bsc.editor.gui.Drawable;
import com.zeusz.bsc.editor.gui.Style;
import com.zeusz.bsc.editor.io.IOManager;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public final class Browser extends Modal implements Drawable {

    private WebView view;

    public Browser() {
        setTitle(Localization.localize("browser.title"));
        init(Size.LARGE);

        view = new WebView();
        view.setPrefWidth(getWidth());
        view.setPrefHeight(getHeight());
        view.setContextMenuEnabled(false);

        // on error
        view.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == Worker.State.FAILED)
                Platform.runLater(() -> getScene().setRoot(getErrorPage()));
        });

        // trigger file downloader
        view.getEngine().locationProperty().addListener((observable, oldValue, newValue) -> {
            downloadFile(newValue);
        });

        draw();
    }

    private void downloadFile(String url) {
        if(url.endsWith(".gwp")) {
            FileChooser chooser = IOManager.getInstance().getFileChooser(false);
            chooser.setInitialFileName(new File(url).getName());

            File file = chooser.showSaveDialog(this);

            if(file != null) {
                try(InputStream stream = new URL(url).openStream();
                    FileOutputStream fos = new FileOutputStream(file)) {
                    byte[] buffer = new byte[stream.available()];
                    stream.read(buffer);
                    fos.write(buffer);
                }
                catch(IOException e) {
                    InfoWindow window = new InfoWindow(Localization.localize("error.save"));
                    window.initOwner(this);
                    window.show();
                }
            }
        }
    }

    private VBox getErrorPage() {
        Text errorMsg = new Text(Localization.localize("browser.error"));
        Button reloadBtn = new Button(Localization.localize("browser.reload"));
        VBox page = new VBox(errorMsg, reloadBtn);

        errorMsg.setFont(Style.FONT_MONOSPACED);
        reloadBtn.setOnAction(event -> draw());
        reloadBtn.setId("reload");
        reloadBtn.getStylesheets().add("css/browser-btn.css");
        page.setSpacing(Style.SPACING_SMALL);
        page.setPadding(Style.PADDING_SMALL);
        page.setAlignment(Pos.BASELINE_CENTER);

        return page;
    }

    @Override
    protected Content getContent() {
        return null;
    }

    @Override
    public void draw() {
        String url = Cloud.getCloudUrl("/user/?lang=" + Localization.getLocale().getLanguage());
        view.getEngine().load(url);
        setScene(new Scene(view));
    }

}
