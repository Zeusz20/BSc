package com.zeusz.bsc.app.dialog;

import android.app.Activity;

import androidx.annotation.Nullable;

import com.zeusz.bsc.app.layout.AttributeList;
import com.zeusz.bsc.core.Localization;
import com.zeusz.bsc.core.Object;


public class AttributeListDialog extends AdapterDialog {

    public AttributeListDialog(Activity ctx, @Nullable Object object) {
        super(ctx, new AttributeList(ctx, object));
        setTitle(Localization.localize((object == null) ? "game.select_attribute" : "game.object_attributes"));
        setPositiveButton(Localization.localize("word.cancel"), GameDialog.DISMISS);
    }

}
