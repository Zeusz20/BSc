package com.zeusz.bsc.editor.validation;

import com.zeusz.bsc.core.GWObject;


public interface ValidationHint {

    void initValidationIcon(GWObject object);

    ValidationIcon getValidationIcon();

    default void validateObject() {
        ValidationIcon icon = getValidationIcon();
        if(icon != null) icon.validateObject();
    }

}
