package com.zeusz.bsc.app.layout;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.zeusz.bsc.app.MainActivity;
import com.zeusz.bsc.app.adapter.AttributeListAdapter;
import com.zeusz.bsc.app.ui.ViewManager;
import com.zeusz.bsc.core.Attribute;
import com.zeusz.bsc.core.Object;
import com.zeusz.bsc.core.Pair;

import java.util.List;
import java.util.stream.Collectors;


public class AttributeList extends AttachedListView {

    public AttributeList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AttributeList(Activity ctx, @Nullable Object object) {
        this(ctx, (AttributeSet) null);

        // show all attribute if object is null, otherwise show object's attributes
        List<Attribute> attributes = (object == null)
                ? ((MainActivity) ctx).getGameClient().getGame().getProject().getItemList(Attribute.class)
                : object.getAttributes().stream().map(Pair::getKey).collect(Collectors.toList());

        setAdapter(new AttributeListAdapter(ctx, attributes, object));

        setOnItemClickListener((parent, view, position, id) -> {
            // showing an alert dialog of all of the attributes in the project,
            // so player can select one for question prep
            if(dialog != null && object == null) {
                Attribute attribute = (Attribute) getItemAtPosition(position);
                ViewManager.buildQuestion(ctx, null, attribute, true);
                dialog.dismiss();
            }
        });
    }

}
