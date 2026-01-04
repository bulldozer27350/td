package com.towerdefense.editor.api.model.draft;

import java.util.ArrayList;
import java.util.List;

public class EditableMap {

	private int width;
	private int height;
	private List<EditablePath> paths = new ArrayList<>();

	@SuppressWarnings("unused")
	// For serialization
	private EditableMap() {
	}

	public EditableMap(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void addPath(EditablePath path) {
		paths.add(path);
	}
	
	public boolean removePath(String pathId) {
		return paths.removeIf(path -> path.id().equals(pathId));
	}

	public List<EditablePath> paths() {
		return new ArrayList<EditablePath>(this.paths);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
