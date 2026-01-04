package com.towerdefense.editor.api.model.editor;

import com.towerdefense.editor.api.model.draft.EditableLevel;
import com.towerdefense.editor.api.model.draft.EditableWave;
import com.towerdefense.editor.api.model.exportable.PathDefinition;

public interface LevelEditor {

	void addPath(PathDefinition path);
	
	boolean removePath(String pathId);

    void addTowerCapacity(String towerCapacityId);

    void addEnemyWave(int attackIndex, EditableWave wave);
    
    EditableLevel getCurrentLevel();
    
    void loadLevel(EditableLevel level);
	
}
