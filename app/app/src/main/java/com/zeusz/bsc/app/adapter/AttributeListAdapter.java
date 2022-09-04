package com.zeusz.bsc.app.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.zeusz.bsc.app.R;
import com.zeusz.bsc.core.Attribute;
import com.zeusz.bsc.core.Pair;

import java.util.List;


public class AttributeListAdapter extends Adapter<Pair<Attribute, String>> {

    public AttributeListAdapter(Activity ctx, List<Pair<Attribute, String>> attributes) {
        super(ctx, R.layout.attribute_item, attributes);
    }

    @Override
    protected View render(View view, int position) {
        Pair<Attribute, String> pair = getItem(position);

        TextView name = view.findViewById(R.id.attribute_name);
        TextView value = view.findViewById(R.id.attribute_value);

        name.setText(pair.getKey().getName());
        value.setText(pair.getValue());

        return view;
    }

}
