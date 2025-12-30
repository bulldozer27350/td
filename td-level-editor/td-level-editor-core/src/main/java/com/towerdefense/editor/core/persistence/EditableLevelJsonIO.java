package com.towerdefense.editor.core.persistence;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.towerdefense.editor.api.model.EditableLevel;

public class EditableLevelJsonIO {

	private final ObjectMapper mapper = new ObjectMapper();

	public void save(EditableLevel config, Path file) throws IOException {
		mapper.writerWithDefaultPrettyPrinter().writeValue(file.toFile(), config);
	}

	public EditableLevel load(Path file) throws IOException {
		File file2 = file.toFile();
		return mapper.readValue(file2, EditableLevel.class);
	}
}
