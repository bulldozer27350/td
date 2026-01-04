package com.towerdefense.editor.core;

import com.towerdefense.editor.core.exporter.editable.EditableGenericExportService;
import com.towerdefense.editor.core.importer.editable.EditableGenericImportService;
import com.towerdefense.editor.core.persistence.EditableGenericObjectJsonIO;
import com.towerdefense.editor.core.validation.editable.DefaultGenericEditableValidator;

public class GenericIOServices<T> {

	private final EditableGenericExportService<T> exportService;
	private final EditableGenericImportService<T> importService;
	private final EditableGenericObjectJsonIO<T> jsonIO;
	
	public GenericIOServices(Class<T> clazz) {
		this.exportService = new EditableGenericExportService<T>(new DefaultGenericEditableValidator<T>());
		this.importService = new EditableGenericImportService<T>();
		this.jsonIO = new EditableGenericObjectJsonIO<T>(clazz);
	}

	public EditableGenericExportService<T> getExportService() {
		return exportService;
	}

	public EditableGenericImportService<T> getImportService() {
		return importService;
	}

	public EditableGenericObjectJsonIO<T> getJsonIO() {
		return jsonIO;
	}
	
}
