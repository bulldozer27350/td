package com.towerdefense.editor.core.validation.editable;

import java.util.ArrayList;
import java.util.List;

public class EditableValidationResult {

    private final List<String> errors = new ArrayList<>();

    public void addError(String error) {
        errors.add(error);
    }

    public boolean isValid() {
        return errors.isEmpty();
    }

    public List<String> getErrors() {
        return errors;
    }
}
