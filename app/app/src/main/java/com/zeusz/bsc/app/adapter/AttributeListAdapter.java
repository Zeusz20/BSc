package com.zeusz.bsc.app.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.zeusz.bsc.app.R;
import com.zeusz.bsc.core.Attribute;
import com.zeusz.bsc.core.Object;

import java.util.List;


public class AttributeListAdapter extends Adapter<Attribute> {

    protected Object object;

    public AttributeListAdapter(Activity ctx, List<Attribute> attributes, @Nullable Object object) {
        super(ctx, R.layout.attribute_item, attributes);
        this.object = object;
    }

    @Override
    protected View render(View view, int position) {
        Attribute attribute = getItem(position);

        TextView name = view.findViewById(R.id.attribute_name);
        TextView value = view.findViewById(R.id.attribute_value);

        name.setText(attribute.getName());

        // show attribute values for selected object
        if(object != null) {
            object.getAttributes().stream().filter(it -> it.getKey().equals(attribute)).findAny().ifPresent(it -> {
                value.setText(it.getValue());
            });
        }

        return view;
    }

}
