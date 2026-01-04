package com.towerdefense.editor.api.level;

import java.util.List;

import com.towerdefense.editor.api.model.draft.EditableAttack;
import com.towerdefense.editor.api.model.draft.EditableLevel;
import com.towerdefense.editor.api.model.draft.EditablePath;

public interface LevelEditorApi {

	EditableLevel getCurrentLevel();
	
	boolean attributeInitialMoney(int initialMoney);
	
	boolean attributeInitialLives(int initialLives);
	
	
	boolean addPath(EditablePath path);
	
	boolean removePath(String pathId);
	
	
	boolean addAttack(EditableAttack attack);
	
	AttackEditorApi getAttackEditor(String attackId);

	List<AttackEditorApi> getAllAttackEditors();
	
	boolean removeAttack(String attackId);
	
	
	boolean addTowerType(String towerTypeId);
	
	boolean removeTowerType(String towerTypeId);
	
}
