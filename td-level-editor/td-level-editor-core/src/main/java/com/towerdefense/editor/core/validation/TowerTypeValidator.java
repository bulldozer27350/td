package com.towerdefense.editor.core.validation;

import com.towerdefense.editor.api.model.draft.EditableTowerType;

public interface TowerTypeValidator {
    ValidationResult validate(EditableTowerType editableTowerType);
}
