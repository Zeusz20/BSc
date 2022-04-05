package com.zeusz.bsc.editor.gui.window;

import com.zeusz.bsc.editor.localization.Localization;


public class SaveWindow extends Modal {

    public SaveWindow() {
        init(Modal.SMALL);
    }

    @Override
    protected Content getContent() {
        return new Content(Localization.capLocalize("window.save_changes"), confirmBtn, discardBtn, cancelBtn);
    }

}
