package com.zeusz.bsc.editor.gui.menu;

import com.zeusz.bsc.editor.event.MenuEvents;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;


public class AccelMenuItem extends MenuItem {

    public AccelMenuItem(String name, String accelerator, String event) {
        super(name);
        setOnAction(MenuEvents.getEventByName(event));
        if(accelerator != null)
            setAccelerator(KeyCombination.valueOf(accelerator));
    }

}
