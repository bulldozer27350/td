package com.towerdefense.editor.api.tower;

import java.util.List;

import com.towerdefense.editor.api.model.draft.EditableTowerRank;
import com.towerdefense.editor.api.model.draft.EditableTowerType;

public interface TowerTypeEditorApi {
	EditableTowerType getCurrentTowerType();
	
	boolean addTowerRank(int rank, int damage, double reloadTime, int cost, double range, int sellReward, int buildTimeTicks);
	
	boolean removeTowerRank(int rank);
	
	EditableTowerRank getTowerRank(int rank);
	
	List<EditableTowerRank> getAllTowerRanks();
	
}
