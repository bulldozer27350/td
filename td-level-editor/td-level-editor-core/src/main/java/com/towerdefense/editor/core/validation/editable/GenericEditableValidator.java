package com.towerdefense.editor.core.validation.editable;

public interface GenericEditableValidator<T> {

	EditableValidationResult validate(T ojectToValidate);
	
}
