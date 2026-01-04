package com.towerdefense.editor.implementation;

import java.util.ArrayList;
import java.util.List;

import com.towerdefense.editor.api.model.draft.EditableTowerLevel;
import com.towerdefense.editor.api.model.draft.EditableTowerType;
import com.towerdefense.editor.api.tower.TowerTypeEditorApi;

public class TowerTypeEditorApiImpl implements TowerTypeEditorApi {

	private final EditableTowerType current;

	public TowerTypeEditorApiImpl(EditableTowerType editableTowerType) {
		this.current = editableTowerType;
	}
	
	@Override
	public EditableTowerType getCurrentTowerType() {
		return current;
	}

	@Override
	public boolean addTowerLevel(int level, int damage, double reloadTime, int cost, double range, int sellReward, int buildTimeTicks) {
		return current.addUpgrade(new EditableTowerLevel(level, cost, range, damage, reloadTime, sellReward, buildTimeTicks));
	}

	@Override
	public boolean removeTowerLevel(int level) {
		return this.current.upgrades().remove(getTowerLevel(level));
	}

	@Override
	public EditableTowerLevel getTowerLevel(int level) {
		return this.current.upgrades().stream().filter(e->e.level() == level).findFirst().orElse(null);
	}

	@Override
	public List<EditableTowerLevel> getAllTowerLevels() {
		return new ArrayList<>(this.current.upgrades());
	}

}
