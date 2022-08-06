package com.zeusz.bsc.app.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;


public abstract class Adapter<T> extends ArrayAdapter<T> {

    protected int itemLayoutID;

    public Adapter(Activity ctx, int itemLayoutID, List<T> items) {
        super(ctx, itemLayoutID, items);
        this.itemLayoutID = itemLayoutID;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            Activity ctx = (Activity) getContext();
            convertView = ctx.getLayoutInflater().inflate(itemLayoutID, parent, false);
        }

        return render(convertView, position);
    }

    protected abstract View render(View view, int position);

}
