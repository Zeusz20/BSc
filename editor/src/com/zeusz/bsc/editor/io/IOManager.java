package com.zeusz.bsc.editor.io;

import com.zeusz.bsc.core.Project;
import com.zeusz.bsc.editor.Editor;
import com.zeusz.bsc.editor.validation.Validation;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.*;


public final class IOManager {

    /* Singleton */
    private static final IOManager INSTANCE = new IOManager();
    public static IOManager getInstance() { return INSTANCE; }

    /* Class methods and fields */
    private File home, images;
    private FileChooser dialog;
    private ExtensionFilter gwpFilter, imageFilter;

    private IOManager() {
        // init filters
        gwpFilter = new ExtensionFilter("GWProject (*.gwp)", "*.gwp");
        imageFilter = new ExtensionFilter("Image (*.png), (*.jpg), (*.jpeg)", "*.png", "*.jpg", "*.jpeg");

        // init home directory
        home = new File(IOManager.class.getProtectionDomain().getCodeSource().getLocation().getFile()); // jar's location
        setImagesDir(home);

        // init file chooser
        dialog = new FileChooser();
        dialog.setInitialDirectory(home);
    }

    public boolean load() {
        File file = getFileChooser(false).showOpenDialog(null);

        if(file != null) {
            try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Project project = (Project) ois.readObject();
                Editor.getInstance().initProject(project);
            }
            catch(Exception e) {
                return false;
            }
        }

        return true;
    }

    public boolean save() {
        Project project = Editor.getInstance().getProject();
        return (project.getSource() != null) ? saveTo(project.getSource()) : saveAs();
    }

    public boolean saveAs() {
        dialog.setInitialFileName(Editor.getInstance().getProject().getName());
        File file = getFileChooser(false).showSaveDialog(null);
        return (file == null) || saveTo(file);  // save only if file is not null
    }

    public boolean saveTo(File destination) {
        Project project = Editor.getInstance().getProject();
        Validation.validate(project);

        // save source file and new hash (order matters because setting the source file changes the hash)
        project.setSource(destination);
        Editor.getInstance().setHash(project.hash());

        // save to file
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(destination))) {
            oos.writeObject(project);
            return true;
        }
        catch(IOException e) {
            return false;
        }
    }

    public void setImagesDir(File file) {
        if(file != null && file.isDirectory())
            images = file;
    }

    public FileChooser getFileChooser(boolean forImages) {
        dialog.getExtensionFilters().clear();
        dialog.getExtensionFilters().add(forImages ? imageFilter : gwpFilter);
        dialog.setInitialDirectory(forImages ? images : home);
        return dialog;
    }

}
