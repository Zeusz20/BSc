package com.zeusz.bsc.editor.gui.window;


public class InfoWindow extends Modal {

    private String message;

    public InfoWindow(String message) {
        this.message = message;
        init(Size.SMALL);
    }

    @Override
    protected Content getContent() {
        return new Content(message, okBtn);
    }

}
