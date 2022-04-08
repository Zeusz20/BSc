package com.zeusz.bsc.editor.gui.menu;

import com.zeusz.bsc.core.GWObject;
import com.zeusz.bsc.core.Project;
import com.zeusz.bsc.editor.gui.Drawable;
import com.zeusz.bsc.editor.gui.IconButton;
import com.zeusz.bsc.editor.gui.workspace.form.Row;
import com.zeusz.bsc.editor.localization.Language;
import com.zeusz.bsc.editor.localization.Localization;
import com.zeusz.bsc.editor.io.ResourceLoader;
import com.zeusz.bsc.editor.validation.ValidationHint;
import com.zeusz.bsc.editor.validation.ValidationIcon;

import javafx.geometry.Orientation;

import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import org.ini4j.Ini;


public class NavigationBar extends BorderPane implements Drawable, ValidationHint {

    /* Load menu configuration file */
    private static Ini MENU_CONFIG;

    static {
        try {
            MENU_CONFIG = new Ini(ResourceLoader.getFile("config/menu.ini"));
        }
        catch(Exception e) {
            MENU_CONFIG = null;
        }
    }

    // Utility function for handling empty values in config
    private static String getKeyCode(String section, String key) {
        String shortcut = MENU_CONFIG.get(section, key);
        return shortcut.equals("") ? null : shortcut;
    }

    /* Class members and fields */
    private MenuBar menuBar;
    private ToolBar toolBar;
    private ValidationIcon icon;

    public NavigationBar(Project project) {
        menuBar = new MenuBar();
        toolBar = new ToolBar();

        initValidationIcon(project);
        draw();

        // add validation icon with padding (display it on the rightmost side)
        toolBar.getItems().addAll(Row.getFiller(), icon);

        setTop(menuBar);
        setCenter(toolBar);
    }

    @Override
    public void draw() {
        if(MENU_CONFIG != null) {
            for(String section: MENU_CONFIG.keySet()) {
                Menu menu = new Menu(Localization.localize("menu." + section));

                for(String key : MENU_CONFIG.get(section).keySet()) {
                    String name = Localization.localize("menu." + key);
                    String shortcut = getKeyCode(section, key);

                    // create menu item
                    MenuItem menuItem = new AccelMenuItem(name, shortcut, key);
                    menu.getItems().add(menuItem);

                    // create toolbar item
                    if(shortcut != null) {
                        String path = "img/" + key + ".png";
                        IconButton button = new IconButton(path, name, shortcut, key);
                        toolBar.getItems().add(button);
                    }
                }
                menuBar.getMenus().add(menu);
                toolBar.getItems().add(new Separator(Orientation.VERTICAL));
            }

            // dynamically generate language menu
            Menu langMenu = new Menu(Localization.localize("menu.language"));
            for(Language language: Language.values()) {
                LangMenuItem menuItem = new LangMenuItem(language);
                menuItem.setSelected(menuItem.getLanguage() == Localization.getLanguage());
                langMenu.getItems().add(menuItem);
            }
            menuBar.getMenus().add(langMenu);
        }
    }

    @Override
    public void initValidationIcon(GWObject object) {
        icon = new ValidationIcon(object);
    }

    @Override
    public ValidationIcon getValidationIcon() {
        return icon;
    }

}
