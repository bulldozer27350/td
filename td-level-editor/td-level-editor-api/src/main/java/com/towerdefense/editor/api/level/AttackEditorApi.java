package com.towerdefense.editor.api.level;

import java.util.List;

import com.towerdefense.editor.api.model.draft.EditableAttack;
import com.towerdefense.editor.api.model.draft.EditableWave;

public interface AttackEditorApi {

	EditableAttack getCurrent();
	
	List<EditableWave> getAllEditableWaves();
	
	EditableWave getEditableWave(String id);
	
	boolean addEditableWave(String id, int startTick, int spawnInterval, int count, String enemyType, String pathId);
	
	boolean removeEditableWave(String id);
	
}
