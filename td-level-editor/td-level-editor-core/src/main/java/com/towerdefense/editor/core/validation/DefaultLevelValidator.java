package com.towerdefense.editor.core.validation;

import com.towerdefense.editor.api.model.EditableLevel;

public class DefaultLevelValidator implements LevelValidator {

    @Override
    public ValidationResult validate(EditableLevel level) {
        ValidationResult result = new ValidationResult();

        if (level.getPaths().isEmpty()) {
            result.addError("Level must define at least one path");
        }

        if (level.getAttacks().isEmpty()) {
            result.addError("Level must define at least one attack");
        }

        if (level.getTowerCapacities().isEmpty()) {
            result.addError("Level must define at least one tower capacity");
        }

        return result;
    }
}
