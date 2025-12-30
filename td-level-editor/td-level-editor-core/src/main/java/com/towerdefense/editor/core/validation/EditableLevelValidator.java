package com.towerdefense.editor.core.validation;

import com.towerdefense.editor.api.model.EditableLevel;

public interface EditableLevelValidator {

	EditableLevelValidationResult validate(EditableLevel level);
	
}
