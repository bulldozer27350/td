package com.towerdefense.editor.core.importer.editable;

/**
 * Rebuilds an EditableLevel from an engine LevelConfig.
 * 
 * This class performs no validation.
 */
public class EditableGenericImportService<T> {

	public T importEditable(T objectToImport) {
		// later add some controls about external ids, etc.
		return objectToImport;
	}

}
