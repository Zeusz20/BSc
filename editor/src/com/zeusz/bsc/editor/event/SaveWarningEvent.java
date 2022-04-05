package com.zeusz.bsc.editor.event;

import com.zeusz.bsc.editor.Editor;
import com.zeusz.bsc.editor.gui.window.Modal;
import com.zeusz.bsc.editor.gui.window.SaveWindow;


public class SaveWarningEvent extends Event {

    private Runnable onFinish;

    public SaveWarningEvent(Runnable onFinish) {
        this.onFinish = onFinish;
    }

    @Override
    public void fire() {
        if(Editor.getInstance().isModified()) {
            SaveWindow window = new SaveWindow();
            window.show();
            window.setOnClose(() -> {
                if(window.getState() == Modal.State.APPLY)
                    MenuEvents.SAVE.handle(null);

                if(window.getState() != Modal.State.CANCEL)
                    if(onFinish != null) onFinish.run();
            });
        }
        else {
            if(onFinish != null) onFinish.run();
        }
    }

}
