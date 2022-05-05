package com.zeusz.bsc.editor.gui.workspace.form;

import com.zeusz.bsc.core.Localization;
import com.zeusz.bsc.core.Object;
import com.zeusz.bsc.editor.gui.Box;
import com.zeusz.bsc.editor.gui.FixedButton;
import com.zeusz.bsc.editor.gui.IconButton;
import com.zeusz.bsc.editor.gui.Prompt;
import com.zeusz.bsc.editor.gui.window.Modal;
import com.zeusz.bsc.editor.gui.window.URLWindow;
import com.zeusz.bsc.editor.io.FXImageCache;
import com.zeusz.bsc.editor.io.IOManager;

import java.io.File;
import java.net.URL;


public class ImageBox extends Box {

    private ImagePreview imagePreview;

    public ImageBox(Object object) {
        Prompt prompt = new Prompt(Localization.prompt("word.image"));
        FixedButton browseBtn = new FixedButton(Localization.localize("form.browse"));
        FixedButton fromUrlBtn = new FixedButton(Localization.localize("form.from_url"));
        ImagePreview imagePreview = new ImagePreview(object.getImage());
        IconButton deleteBtn = new IconButton("img/del.png");

        browseBtn.setOnAction(event -> {
            File file = IOManager.getInstance().getFileChooser(true).showOpenDialog(null);
            if(file != null && imagePreview.load(file.getPath(), false))
                FXImageCache.getInstance().cacheImage(object, imagePreview.getImage());
        });

        fromUrlBtn.setOnAction(event -> {
            URLWindow window = new URLWindow();
            window.show();
            window.setOnClose(() -> {
                if(window.getState() == Modal.State.APPLY) {
                    URL url = window.getUrl();
                    if(url != null && imagePreview.load(url.toString(), true))
                        FXImageCache.getInstance().cacheImage(object, imagePreview.getImage());
                }
            });
        });

        deleteBtn.setOnAction(event -> {
            imagePreview.preview(null);
            FXImageCache.getInstance().disposeImage(object);
        });

        this.imagePreview = imagePreview;
        getChildren().addAll(new Row(prompt, browseBtn, fromUrlBtn, Row.getFiller(), deleteBtn), imagePreview);
    }

    public ImagePreview getImagePreview() {
        return imagePreview;
    }

}
