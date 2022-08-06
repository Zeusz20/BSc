package com.zeusz.bsc.app.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.zeusz.bsc.app.R;
import com.zeusz.bsc.core.Attribute;

import java.util.List;


public class AttributeListAdapter extends Adapter<Attribute> {

    public AttributeListAdapter(Activity ctx, List<Attribute> attributes) {
        super(ctx, R.layout.attribute_item, attributes);
    }

    @Override
    protected View render(View view, int position) {
        Attribute attribute = getItem(position);

        TextView name = view.findViewById(R.id.attribute_name);
        TextView value = view.findViewById(R.id.attribute_value);

        name.setText(attribute.getName());
        // TODO: get own object attributes

        return view;
    }

}
