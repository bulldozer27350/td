package com.towerdefense.editor.core.exporter;

import com.towerdefense.editor.api.model.EditableLevel;
import com.towerdefense.editor.core.validation.EditableLevelValidationResult;
import com.towerdefense.editor.core.validation.EditableLevelValidator;

public class EditableLevelExportService {

	private final EditableLevelValidator validator;
	
	public EditableLevelExportService(EditableLevelValidator validator) {
		this.validator = validator;
	}
	
	public void export(EditableLevel level) {
		EditableLevelValidationResult validation = validator.validate(level);
		if (!validation.isValid()) {
			throw new IllegalStateException("Invalid level: " + validation.getErrors());
		}
	}
	
}
