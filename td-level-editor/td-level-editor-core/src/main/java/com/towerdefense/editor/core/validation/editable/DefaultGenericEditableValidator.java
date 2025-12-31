package com.towerdefense.editor.core.validation.editable;

public class DefaultGenericEditableValidator<T> implements GenericEditableValidator<T> {

    @Override
    public EditableValidationResult validate(T objectToValidate) {
        EditableValidationResult result = new EditableValidationResult();
        return result;
    }
}
