package com.zeusz.bsc.app.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeusz.bsc.app.R;
import com.zeusz.bsc.core.Pair;

import java.util.ArrayList;


public class HistoryAdapter extends Adapter<Pair<String, Boolean>> {

    public HistoryAdapter(Activity ctx) {
        super(ctx, R.layout.history_item, new ArrayList<>());
    }

    @Override
    protected View render(View view, int position) {
        Pair<String, Boolean> item = getItem(position);
        ImageView imageView = view.findViewById(R.id.history_item_icon);
        TextView textView = view.findViewById(R.id.history_item_text);

        imageView.setImageResource(item.getValue() ? R.drawable.positive_feedback : R.drawable.negative_feedback);
        textView.setText(item.getKey());

        return view;
    }

}
