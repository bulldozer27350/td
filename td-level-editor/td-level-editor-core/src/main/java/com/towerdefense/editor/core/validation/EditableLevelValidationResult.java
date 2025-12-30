package com.towerdefense.editor.core.validation;

import java.util.ArrayList;
import java.util.List;

public class EditableLevelValidationResult {

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
