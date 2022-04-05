package com.zeusz.bsc.editor.gui.window;

import com.zeusz.bsc.editor.gui.Box;
import com.zeusz.bsc.editor.gui.Prompt;

import javafx.scene.control.TextField;

import java.net.MalformedURLException;
import java.net.URL;


public class URLWindow extends Modal {

    private TextField url;

    public URLWindow() {
        url = new TextField();
        init(Modal.SMALL);

        url.requestFocus();
    }

    public URL getUrl() {
        try {
            return new URL(url.getText());
        }
        catch(MalformedURLException e) {
            return null;
        }
    }

    @Override
    protected Content getContent() {
        return new Content(new Box(new Prompt("URL:"), url), okBtn, cancelBtn);
    }

}
