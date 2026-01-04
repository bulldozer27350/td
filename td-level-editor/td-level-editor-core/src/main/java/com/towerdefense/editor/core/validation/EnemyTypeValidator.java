package com.towerdefense.editor.core.validation;

import com.towerdefense.editor.api.model.draft.EditableEnemyType;

public interface EnemyTypeValidator {
    ValidationResult validate(EditableEnemyType level);
}
