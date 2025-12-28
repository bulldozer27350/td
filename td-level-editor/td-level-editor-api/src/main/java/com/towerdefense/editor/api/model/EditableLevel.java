package com.towerdefense.editor.api.model;

import java.util.ArrayList;
import java.util.List;

public class EditableLevel {

	private LevelMetadata metadata;
	private EditableMap map;
	private List<EditablePath> paths = new ArrayList<>();
	private List<EditableAttack> attacks = new ArrayList<>();
	private List<EditableTowerCapacity> towerCapacities = new ArrayList<>();

	public EditableLevel(LevelMetadata metadata, EditableMap editableMap, List<EditableAttack> attacks,
			List<EditableTowerCapacity> towerCapacities) {
		this.metadata = metadata;
		this.map = editableMap;
		this.attacks = attacks;
		this.towerCapacities = towerCapacities;
		this.paths = editableMap.getPaths();
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

	public List<EditableTowerCapacity> getTowerCapacities() {
		return towerCapacities;
	}
}
