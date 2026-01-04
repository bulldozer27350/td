package com.towerdefense.editor.api.model.draft;

import java.util.ArrayList;
import java.util.List;

import com.towerdefense.editor.api.model.exportable.LevelMetadata;

public class EditableLevel {

	private LevelMetadata metadata;
	private EditableMap map;
	private List<EditablePath> paths = new ArrayList<>();
	private List<EditableAttack> attacks = new ArrayList<>();
	private List<String> towerCapcityIds = new ArrayList<String>();

	@SuppressWarnings("unused")
	// For serialization
	private EditableLevel() {
	}
	
	public EditableLevel(LevelMetadata metadata, EditableMap editableMap, List<EditableAttack> attacks,
			List<String> towerCapacities) {
		this.metadata = metadata;
		this.map = editableMap;
		this.attacks = attacks;
		this.paths = editableMap.paths();
	}

	public LevelMetadata getMetadata() {
		return metadata;
	}

	public EditableMap getMap() {
		return map;
	}

	public List<EditablePath> getPaths() {
		return paths;
	}

	public List<EditableAttack> getAttacks() {
		return attacks;
	}
	
	public List<String> getTowerCapacityIds() {
		return towerCapcityIds;
	}

}