package com.towerdefense.editor.api.model;

import java.util.ArrayList;
import java.util.List;

public class EditableMap {

    private final int width;
    private final int height;
    private final List<EditablePath> paths = new ArrayList<>();

    public EditableMap(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void addPath(EditablePath path) {
        paths.add(path);
    }

    public List<EditablePath> paths() { return paths; }

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public List<EditablePath> getPaths() {
		return paths;
	}
    
}

