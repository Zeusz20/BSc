package com.zeusz.bsc.editor.gui.window;

import com.zeusz.bsc.core.GWObject;
import com.zeusz.bsc.editor.gui.Drawable;
import com.zeusz.bsc.editor.io.IOManager;
import com.zeusz.bsc.editor.localization.Localization;
import com.zeusz.bsc.editor.io.ResourceLoader;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;

import netscape.javascript.JSObject;

import org.w3c.dom.Element;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


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
        String url = GWObject.CLOUD_URL + "/user/?lang=" + Localization.getLanguage().getTag();
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
                URL resource = ResourceLoader.getResource("browser/404.html");
                if(resource != null)
                    view.getEngine().load(resource.toExternalForm());
            });
        }
        else if(state == Worker.State.SUCCEEDED && e404) {
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
