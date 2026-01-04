package com.towerdefense.editor.itf;

import java.util.List;

import com.towerdefense.editor.api.level.LevelEditorApi;
import com.towerdefense.editor.api.model.draft.EditableLevel;

public interface LevelsCollectionEditorApi {
	LevelEditorApi createLevel(String id, int width, int height);

	LevelEditorApi loadDraftLevel(String path);

	LevelEditorApi loadExportableLevel(String path);

	boolean removeLevel(String id);
	
	boolean saveDraftLevel(String id, String path);
	
	boolean saveExportableLevel(String id, String path);
	
	EditableLevel getEditableLevel(String id);
	
	List<EditableLevel> getAllEditableLevels();
}
