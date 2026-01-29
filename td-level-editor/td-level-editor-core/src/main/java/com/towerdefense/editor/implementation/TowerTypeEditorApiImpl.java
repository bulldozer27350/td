package com.towerdefense.editor.implementation;

import java.util.ArrayList;
import java.util.List;

import com.towerdefense.editor.api.model.draft.EditableTowerRank;
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
	public boolean addTowerRank(int rank, int damage, double reloadTime, int cost, double range, int sellReward, int buildTimeTicks) {
		return current.addRank(new EditableTowerRank(rank, cost, range, damage, reloadTime, sellReward, buildTimeTicks));
	}

	@Override
	public boolean removeTowerRank(int rank) {
		return this.current.ranks().remove(getTowerRank(rank));
	}

	@Override
	public EditableTowerRank getTowerRank(int rank) {
		return this.current.ranks().stream().filter(e->e.rank() == rank).findFirst().orElse(null);
	}

	@Override
	public List<EditableTowerRank> getAllTowerRanks() {
		return new ArrayList<>(this.current.ranks());
	}

}
