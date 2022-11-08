package com.zeusz.bsc.app.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.zeusz.bsc.app.R;

import java.util.List;


public class ValueListAdapter extends Adapter<String> {

    public ValueListAdapter(Activity ctx, int itemLayoutId, List<String> values) {
        super(ctx, itemLayoutId, values);
    }

    @Override
    protected View render(View view, int position) {
        String value = getItem(position);
        TextView valueView = view.findViewById(R.id.attribute_value);

        int background = view.getContext().getResources().getColor(R.color.light_gray, null);
        int textColor = view.getContext().getResources().getColor(R.color.dark_gray, null);

        valueView.setText(value);
        valueView.setTextColor(textColor);
        valueView.setBackgroundColor(background);
        valueView.setPadding(2, 2, 2, 2);

        return view;
    }

}
