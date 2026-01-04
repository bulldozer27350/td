package com.towerdefense.editor.core.validation;

import com.towerdefense.editor.api.model.draft.EditableLevel;

public interface LevelValidator {
    ValidationResult validate(EditableLevel level);
}
