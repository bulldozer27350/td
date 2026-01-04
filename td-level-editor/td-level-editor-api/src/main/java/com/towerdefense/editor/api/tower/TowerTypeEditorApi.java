package com.towerdefense.editor.api.tower;

import java.util.List;

import com.towerdefense.editor.api.model.draft.EditableTowerLevel;
import com.towerdefense.editor.api.model.draft.EditableTowerType;

public interface TowerTypeEditorApi {
	EditableTowerType getCurrentTowerType();
	
	boolean addTowerLevel(int level, int damage, double reloadTime, int cost, double range, int sellReward, int buildTimeTicks);
	
	boolean removeTowerLevel(int level);
	
	EditableTowerLevel getTowerLevel(int level);
	
	List<EditableTowerLevel> getAllTowerLevels();
	
}
