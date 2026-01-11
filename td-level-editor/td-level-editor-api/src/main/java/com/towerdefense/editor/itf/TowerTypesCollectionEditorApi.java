package com.towerdefense.editor.itf;

import java.util.List;

import com.towerdefense.editor.api.model.draft.EditableTowerType;
import com.towerdefense.editor.api.tower.TowerTypeEditorApi;

public interface TowerTypesCollectionEditorApi {
	TowerTypeEditorApi createTowerType(String id, String name);

	TowerTypeEditorApi loadDraftTowerType(String path);

	TowerTypeEditorApi loadExportableTowerType(String path);

	boolean removeTowerType(String id);
	
	boolean saveDraftTowerType(String id, String path);
	
	boolean saveExportableTowerType(String id, String path);
	
	TowerTypeEditorApi getTowerTypeEditor(String id);
	
	EditableTowerType getEditableTowerType(String id);
	
	List<EditableTowerType> getAllEditableTowerTypes();
}
