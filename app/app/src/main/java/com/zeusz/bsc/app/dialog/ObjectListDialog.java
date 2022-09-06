package com.zeusz.bsc.app.dialog;

import android.app.Activity;

import com.zeusz.bsc.app.layout.ObjectList;
import com.zeusz.bsc.core.Localization;
import com.zeusz.bsc.core.Object;

import java.util.List;


public class ObjectListDialog extends AdapterDialog {

    public ObjectListDialog(Activity ctx, List<Object> objects) {
        super(ctx, new ObjectList(ctx, objects));
        setTitle(Localization.localize("game.choose_object"));
        setPositiveButton(Localization.localize("word.cancel"), GameDialog.DISMISS);
    }

}
