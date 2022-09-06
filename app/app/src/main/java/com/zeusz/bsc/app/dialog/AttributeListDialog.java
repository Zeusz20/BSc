package com.zeusz.bsc.app.dialog;

import android.app.Activity;
import android.app.AlertDialog;

import androidx.annotation.Nullable;

import com.zeusz.bsc.app.layout.AttributeList;
import com.zeusz.bsc.core.Localization;
import com.zeusz.bsc.core.Object;


public class AttributeListDialog extends GameDialog {

    protected AttributeList attributes;

    public AttributeListDialog(Activity ctx, @Nullable Object object) {
        super(ctx);
        attributes = new AttributeList(ctx, object);

        setTitle(Localization.localize((object == null) ? "game.select_attribute" : "game.object_attributes"));
        setView(attributes);
    }

    @Override
    public AlertDialog create() {
        AlertDialog dialog = super.create();
        attributes.setDialog(dialog);
        return dialog;
    }

}
