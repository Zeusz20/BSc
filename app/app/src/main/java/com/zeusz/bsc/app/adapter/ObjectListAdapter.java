package com.zeusz.bsc.app.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeusz.bsc.app.R;
import com.zeusz.bsc.app.io.IOManager;
import com.zeusz.bsc.core.Object;

import java.util.List;


public class ObjectListAdapter extends Adapter<Object> {

    public ObjectListAdapter(Activity ctx, List<Object> objects) {
        super(ctx, R.layout.object_item, objects);
    }

    @Override
    protected View render(View view, int position) {
        Object object = getItem(position);

        ImageView imageView = view.findViewById(R.id.object_image);
        TextView nameView = view.findViewById(R.id.object_name);

        imageView.setImageBitmap(IOManager.getImage(object.getImage()));
        nameView.setText(object.getName());

        return view;
    }

}
