package com.towerdefense.editor.itf;

import java.util.List;

import com.towerdefense.editor.api.model.draft.EditableEnemyType;

public interface EnemyTypeEditorApi {
	boolean createEnemyType(String id, int hp, double speed, int reward);

	boolean loadDraftEnemyType(String path);

	boolean loadExportableEnemyType(String path);

	boolean removeEnemyType(String id);
	
	List<EditableEnemyType> getAllEditableEnemyTypes();

	EditableEnemyType getEditableEnemyType(String id);
	
	boolean saveDraftEnemyType(String id, String path);
	
	boolean saveExportableEnemyType(String id, String path);
}
