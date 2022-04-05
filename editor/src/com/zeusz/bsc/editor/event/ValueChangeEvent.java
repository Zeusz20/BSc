package com.zeusz.bsc.editor.event;

import com.zeusz.bsc.core.Attribute;
import com.zeusz.bsc.core.Object;
import com.zeusz.bsc.core.Pair;
import com.zeusz.bsc.editor.Editor;


/** If an {@link Attribute}'s value is changed, changes it's value in associated {@link Object} instances. */
public class ValueChangeEvent extends Event {

    private Attribute attribute;
    private String oldValue, newValue;

    public ValueChangeEvent(Attribute attribute, String oldValue, String newValue) {
        this.attribute = attribute;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @Override
    public void fire() {
        for(Object object: Editor.getInstance().getProject().getItemList(Object.class)) {
            for(Pair<Attribute, String> pair : object.getAttributes()) {
                if(pair.getKey().equals(attribute) && pair.getValue().equals(oldValue)) {
                    pair.setValue(newValue);
                }
            }
        }
    }

}
