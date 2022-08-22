package com.zeusz.bsc.app.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;


public abstract class Adapter<T> extends ArrayAdapter<T> {

    protected int itemLayoutId;

    public Adapter(Activity ctx, int itemLayoutId, List<T> items) {
        super(ctx, itemLayoutId, items);
        this.itemLayoutId = itemLayoutId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            Activity ctx = (Activity) getContext();
            convertView = ctx.getLayoutInflater().inflate(itemLayoutId, parent, false);
        }

        return render(convertView, position);
    }

    protected abstract View render(View view, int position);

}
