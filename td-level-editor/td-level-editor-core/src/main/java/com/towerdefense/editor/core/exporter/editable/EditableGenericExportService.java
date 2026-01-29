package com.towerdefense.editor.core.exporter.editable;

import com.towerdefense.editor.core.validation.editable.EditableValidationResult;
import com.towerdefense.editor.core.validation.editable.GenericEditableValidator;

public class EditableGenericExportService<T> {

	private final GenericEditableValidator<T> validator;
	
	public EditableGenericExportService(GenericEditableValidator<T> validator) {
		this.validator = validator;
	}
	
	public void export(T objectToExport) {
		EditableValidationResult validation = validator.validate(objectToExport);
		if (!validation.isValid()) {
			throw new IllegalStateException("Invalid rank: " + validation.getErrors());
		}
	}
	
}
