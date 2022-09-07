package com.zeusz.bsc.app.layout;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import com.zeusz.bsc.app.MainActivity;
import com.zeusz.bsc.app.adapter.ObjectListAdapter;
import com.zeusz.bsc.app.util.Dictionary;
import com.zeusz.bsc.app.widget.SendButton;
import com.zeusz.bsc.core.Object;

import java.util.List;


public class ObjectList extends AttachedListView {

    public ObjectList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObjectList(Activity ctx, List<Object> objects) {
        this(ctx, (AttributeSet) null);
        setAdapter(new ObjectListAdapter(ctx, objects));
        setClickable(true);
        setOnItemClickListener((parent, view, position, id) -> chooseOrGuessObject(ctx, position));
    }

    private void chooseOrGuessObject(Activity ctx, int position) {
        Object object = (Object) getItemAtPosition(position);

        if(dialog == null) {
            // select object at the beginning of the game
            ((MainActivity) ctx).getGameClient().selectObject(object);
        }
        else {
            // guess opponent's object
            dialog.dismiss();  // dismiss dialog first, so it doesn't have to wait for server to process the request
            SendButton.toggleAll(ctx);
            ((MainActivity) ctx).getGameClient().sendJSON(new Dictionary().put("object", object.getName()));
        }
    }

}
