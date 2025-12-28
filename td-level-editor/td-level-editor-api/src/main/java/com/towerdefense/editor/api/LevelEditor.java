package com.towerdefense.editor.api;

import com.towerdefense.editor.api.model.EditableLevel;
import com.towerdefense.editor.api.model.EditableWave;
import com.towerdefense.editor.api.model.PathDefinition;
import com.towerdefense.editor.api.model.TowerCapacity;
import com.towerdefense.editor.api.model.TowerUpgrade;

public interface LevelEditor {

	void addPath(PathDefinition path);

    void addTowerCapacity(TowerCapacity towerCapacity);

    void addEnemyWave(int attackIndex, EditableWave wave);

    void upgradeTowerType(String towerType, TowerUpgrade upgrade);

    EditableLevel getCurrentLevel();
    
    void loadLevel(EditableLevel level);
	
}
