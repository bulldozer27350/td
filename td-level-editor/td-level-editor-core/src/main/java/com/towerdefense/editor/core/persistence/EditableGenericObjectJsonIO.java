package com.towerdefense.editor.core.persistence;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import com.fasterxml.jackson.databind.ObjectMapper;

public class EditableGenericObjectJsonIO<T> {

	private final ObjectMapper mapper = new ObjectMapper();
	private final Class<T> genericClass;

	public EditableGenericObjectJsonIO(Class<T> editableGenericClass) {
		this.genericClass = editableGenericClass;
	}
	
	public void save(T obj, Path file) throws IOException {
		mapper.writerWithDefaultPrettyPrinter().writeValue(file.toFile(), obj);
	}

	public T load(Path file) throws IOException {
		File file2 = file.toFile();
		return mapper.readValue(file2, genericClass);
	}
}
