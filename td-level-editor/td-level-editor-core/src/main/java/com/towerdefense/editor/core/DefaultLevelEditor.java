package com.towerdefense.editor.core;

import java.util.ArrayList;
import java.util.List;

import com.towerdefense.editor.api.model.draft.EditableAttack;
import com.towerdefense.editor.api.model.draft.EditableLevel;
import com.towerdefense.editor.api.model.draft.EditableMap;
import com.towerdefense.editor.api.model.draft.EditablePath;
import com.towerdefense.editor.api.model.draft.EditableWave;
import com.towerdefense.editor.api.model.draft.TowerCapacity;
import com.towerdefense.editor.api.model.editor.LevelEditor;
import com.towerdefense.editor.api.model.exportable.PathDefinition;

public class DefaultLevelEditor implements LevelEditor {

	private EditableLevel level;
	private final EditableMap map;
	private final List<EditableAttack> attacks;
	private final List<TowerCapacity> towerCapacityIds;

	public DefaultLevelEditor(String id, int startingMoney, int startingLives, int mapWidth, int mapHeight) {
		this.map = new EditableMap(mapWidth, mapHeight);
		this.attacks = new ArrayList<>();
		this.towerCapacityIds = new ArrayList<>();

		this.level = new EditableLevel(id, startingMoney, startingLives, map, attacks, towerCapacityIds);
	}

	@Override
	public void addPath(PathDefinition path) {
		map.addPath(new EditablePath(path.id(), path.points()));
	}

	@Override
	public void addTowerCapacity(TowerCapacity towerCapacityId) {
		towerCapacityIds.add(towerCapacityId);
	}

	@Override
	public void addEnemyWave(int attackIndex, EditableWave wave) {
		attacks.get(attackIndex).addWave(wave);
	}

	@Override
	public EditableLevel getCurrentLevel() {
		return level;
	}

	@Override
	public void loadLevel(EditableLevel level) {
		this.level = level;
	}

	@Override
	public boolean removePath(String pathId) {
		return map.removePath(pathId);
	}
}
