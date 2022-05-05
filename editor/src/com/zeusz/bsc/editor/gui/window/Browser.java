package com.zeusz.bsc.editor.gui.window;

import com.zeusz.bsc.core.Cloud;
import com.zeusz.bsc.core.Localization;
import com.zeusz.bsc.editor.gui.Drawable;
import com.zeusz.bsc.editor.io.IOManager;
import com.zeusz.bsc.editor.io.ResourceLoader;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;

import netscape.javascript.JSObject;

import org.w3c.dom.Element;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;


public final class Browser extends Modal implements Drawable {

    private WebView view;
    private boolean e404;

    public Browser() {
        setTitle(Localization.localize("browser.title"));
        init(Size.LARGE);

        view = new WebView();
        view.setPrefWidth(this.getWidth());
        view.setPrefHeight(this.getHeight());
        view.setContextMenuEnabled(false);

        // on 404
        view.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            onTimeout(newValue);
        });

        // trigger file downloader
        view.getEngine().locationProperty().addListener((observable, oldValue, newValue) -> {
            downloadFile(newValue);
        });

        // invoke java from js
        JSObject object = (JSObject) view.getEngine().executeScript("window");
        object.setMember("browser", this);

        draw();
    }

    @Override
    public void draw() {
        String url = Cloud.getCloudUrl("/user/?lang=" + Localization.getLocale().getLanguage());
        view.getEngine().load(url);
        setScene(new Scene(view));
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

    private void onTimeout(Worker.State state) {
        if(state == Worker.State.FAILED) {
            Platform.runLater(() -> {
                e404 = true;

                // load 404 html page
                try(InputStream file = ResourceLoader.getFile("browser/404.html");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(file, StandardCharsets.UTF_8))) {

                    String content = reader.lines().collect(Collectors.joining("\n"));
                    view.getEngine().loadContent(content);
                }
                catch(IOException e) {
                    // couldn't read html file
                }
            });
        }
        else if(state == Worker.State.SUCCEEDED && e404) {
            // successfully loaded 404 page
            Element message = view.getEngine().getDocument().getElementById("message");
            message.setTextContent(Localization.localize("browser.e404"));

            Element refresh = view.getEngine().getDocument().getElementById("refresh");
            refresh.setTextContent(Localization.localize("browser.refresh"));

            e404 = false;
        }
    }

    @Override
    protected Content getContent() {
        return null;
    }

}
