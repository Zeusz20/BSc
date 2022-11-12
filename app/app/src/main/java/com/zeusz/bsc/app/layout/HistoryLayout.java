package com.zeusz.bsc.app.layout;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.zeusz.bsc.app.R;
import com.zeusz.bsc.app.adapter.HistoryAdapter;
import com.zeusz.bsc.app.ui.ViewManager;
import com.zeusz.bsc.core.Pair;


public class HistoryLayout extends ListView {

    public HistoryLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HistoryLayout(Activity ctx) {
        super(ctx);
        int padding = ViewManager.pixelsToDip(ctx, 8);

        setPadding(padding, padding, padding, padding);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, ViewManager.pixelsToDip(ctx, 255)));
        setBackgroundResource(R.drawable.adapter_bg);
        setAdapter(new HistoryAdapter(ctx));
        setId(R.id.history_layout);
    }

    public void add(Activity ctx, String questionOrObject, Boolean answer) {
        ctx.runOnUiThread(() -> {
            HistoryAdapter adapter = (HistoryAdapter) getAdapter();
            adapter.add(new Pair<>(questionOrObject, answer));
            adapter.notifyDataSetChanged();
        });
    }

}
