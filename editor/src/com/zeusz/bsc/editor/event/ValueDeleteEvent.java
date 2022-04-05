package com.zeusz.bsc.editor.event;

import com.zeusz.bsc.core.Attribute;
import com.zeusz.bsc.core.Object;
import com.zeusz.bsc.core.Pair;
import com.zeusz.bsc.editor.Editor;


/** If an {@link Attribute}'s value is deleted, removes it's reference from all {@link Object} instances. */
public class ValueDeleteEvent extends Event {

    private String value;

    public ValueDeleteEvent(String value) {
        this.value = value;
    }

    @Override
    public void fire() {
        for(Object object: Editor.getInstance().getProject().getItemList(Object.class)) {
            for(Pair<Attribute, String> pair: object.getAttributes()) {
                if(pair.getValue().equals(value)) {
                    object.getAttributes().remove(pair);
                    break;
                }
            }
        }
    }

}
