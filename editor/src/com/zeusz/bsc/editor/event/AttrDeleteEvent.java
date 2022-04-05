package com.zeusz.bsc.editor.event;

import com.zeusz.bsc.core.*;
import com.zeusz.bsc.core.Object;
import com.zeusz.bsc.editor.Editor;


/** If an {@link Attribute} is deleted, removes it's references from {@link Object} and {@link Question} instances. */
public class AttrDeleteEvent extends Event {

    private Attribute attribute;

    public AttrDeleteEvent(Attribute attribute) {
        this.attribute = attribute;
    }

    private void modifyObjects() {
        for(Object object: Editor.getInstance().getProject().getItemList(Object.class)) {
            // prevent ConcurrentModificationException
            int index = 0;
            while(index != object.getAttributes().size()) {
                Attribute attribute = object.getAttributes().get(index).getKey();

                if(attribute.equals(this.attribute))
                    object.getAttributes().remove(index);
                else
                    index++;
            }
        }
    }

    private void modifyQuestions() {
        for(Question question: Editor.getInstance().getProject().getItemList(Question.class)) {
            if(attribute.equals(question.getAttribute())) {
                question.setAttribute(null);
            }
        }
    }

    @Override
    public void fire() {
        modifyObjects();
        modifyQuestions();
    }

}
