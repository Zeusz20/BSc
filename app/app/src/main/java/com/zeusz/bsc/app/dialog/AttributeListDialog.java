package com.zeusz.bsc.app.dialog;

import android.app.Activity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;

import com.google.android.flexbox.FlexboxLayout;

import com.zeusz.bsc.app.R;
import com.zeusz.bsc.app.layout.AttributeList;
import com.zeusz.bsc.core.Localization;
import com.zeusz.bsc.core.Object;


public class AttributeListDialog extends AdapterDialog {

    public AttributeListDialog(Activity ctx, @Nullable Object object) {
        super(ctx, new AttributeList(ctx, object));
        setTitle(Localization.localize((object == null) ? "game.select_attribute" : "game.object_attributes"));
        setPositiveButton(Localization.localize("word.cancel"), GameDialog.DISMISS);
        setOnDismissListener(listener -> highlightQuestion(ctx));
    }

    private void highlightQuestion(Activity ctx) {
        FlexboxLayout question = ctx.findViewById(R.id.question_wrapper);
        Animation highlight = AnimationUtils.loadAnimation(ctx, R.anim.highlight_question);

        question.startAnimation(highlight);
    }

}
