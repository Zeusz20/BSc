package com.zeusz.bsc.editor;

import com.sun.istack.internal.Nullable;

import com.zeusz.bsc.core.*;
import com.zeusz.bsc.core.Object;
import com.zeusz.bsc.editor.event.AttrDeleteEvent;
import com.zeusz.bsc.editor.gui.explorer.ItemLabel;
import com.zeusz.bsc.editor.gui.Scrollable;
import com.zeusz.bsc.editor.gui.explorer.Tab;
import com.zeusz.bsc.editor.gui.workspace.Workspace;
import com.zeusz.bsc.editor.gui.menu.NavigationBar;
import com.zeusz.bsc.editor.gui.explorer.SideMenu;
import com.zeusz.bsc.editor.io.FXImageCache;
import com.zeusz.bsc.editor.localization.Localization;

import javafx.application.Platform;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;


public final class Editor {

    /* Singleton */
    private static final Editor INSTANCE = new Editor();
    public static Editor getInstance() { return INSTANCE; }

    /* Class methods and fields */
    private Project project;
    private byte[] hash;

    private Stage window;
    private BorderPane root;

    private NavigationBar navigationBar;
    private SideMenu sideMenu;

    @Nullable
    private Workspace<? extends Item> workspace;

    private Editor() {
        root = new BorderPane();
        initProject(new Project(Localization.localize("word.project")));
    }

    public void initProject(Project project) {
        this.project = project;

        // validation hint needs to be reinitialized
        navigationBar = new NavigationBar(project);

        // sideMenu needs to be redrawn upon loading a project (starts out closed)
        sideMenu = new SideMenu();

        Item item = findFirstItem();
        workspace = (item != null) ? Workspace.getWorkspace(item) : null;

        // wait for project to finish initialization then open all tabs
        if(item != null) {
            Platform.runLater(() -> {
                sideMenu.getChildren().stream().map(it -> (Tab) it).forEach(it -> { it.toggle(); it.draw(); });
                SideMenu.setActiveLabel(sideMenu.getTabByType(item.getClass()).getItems().get(0));
            });
        }

        root.setTop(navigationBar);
        root.setLeft(new Scrollable(sideMenu));
        initWorkspace(workspace);

        this.hash = this.project.hash();
    }

    public void initWorkspace(Workspace<?> workspace) {
        this.workspace = workspace;
        root.setCenter(this.workspace);
    }

    private Item findFirstItem() {
        // cast items as base class so lookups can be chained
        Function<? super Item, Item> castAs = item -> (Item) item;

        /* Iterate over all items (objects, attributes, questions)
         * in the project and find first one. */
        Item item = project.getItemList(Object.class).stream().map(castAs).findFirst().orElseGet(() -> {
            return project.getItemList(Attribute.class).stream().map(castAs).findFirst().orElseGet(() -> {
                return project.getItemList(Question.class).stream().map(castAs).findFirst().orElse(null);
            });
        });

        return item;
    }

    @SuppressWarnings("unchecked")
    public void addItem(Item item) {
        if(item == null) return;

        // parameter is (capture of ? extends Item)
        ((List<Item>) project.getItemList(item.getClass())).add(item);

        // open and redraw tab if closed
        Tab tab = sideMenu.getTabByType(item.getClass());
        if(tab.isClosed()) tab.toggle();
        tab.draw();

        // display item in workspace
        initWorkspace(Workspace.getWorkspace(item));

        // set new item as active
        int last = tab.getItems().size() - 1;
        ItemLabel label = tab.getItems().get(last);
        SideMenu.setActiveLabel(label);

        // validate project and show on navigation bar
        validateProject();
    }

    public void removeItem(Item item) {
        if(item == null) return;

        if(item instanceof Object) {
            FXImageCache.getInstance().disposeImage((Object) item);
        }
        else if(item instanceof Attribute) {
            // retroactively delete Attribute references in Objects and Questions
            new AttrDeleteEvent((Attribute) item).fire();
        }

        project.getItemList(item.getClass()).remove(item);   // remove item from project
        sideMenu.getTabByType(item.getClass()).draw();       // update side menu

        // validate project and show on navigation bar
        validateProject();
    }

    public void validateProject() {
        // validate item
        if(workspace != null) {
            workspace.save();   // save previous work
            workspace.validateObject();
        }

        // validate whole project
        navigationBar.validateObject();
    }

    public boolean isModified() {
        // compare initial hash with current hash
        return !Arrays.equals(this.hash, project.hash());
    }

    public void setHash(byte[] hash) { this.hash = hash; }

    public byte[] getHash() { return hash; }

    public Project getProject() { return project; }

    public Workspace<?> getWorkspace() { return workspace; }

    public BorderPane getRoot() { return root; }

    public void setWindow(Stage stage) { window = stage; }

    public Stage getWindow() { return window; }

}
