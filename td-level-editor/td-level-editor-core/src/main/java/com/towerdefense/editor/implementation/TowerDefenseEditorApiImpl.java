package com.towerdefense.editor.implementation;

import java.util.List;

import com.towerdefense.editor.api.TowerDefenseEditorApi;
import com.towerdefense.editor.api.level.LevelEditorApi;
import com.towerdefense.editor.api.model.draft.EditableEnemyType;
import com.towerdefense.editor.api.model.draft.EditableLevel;
import com.towerdefense.editor.api.model.draft.EditableTowerType;
import com.towerdefense.editor.api.tower.TowerTypeEditorApi;
import com.towerdefense.editor.itf.EnemyTypeEditorApi;
import com.towerdefense.editor.itf.LevelsCollectionEditorApi;
import com.towerdefense.editor.itf.TowerTypesCollectionEditorApi;

public class TowerDefenseEditorApiImpl implements TowerDefenseEditorApi {

	private final LevelsCollectionEditorApi levelsCollectionEditorApi;
	private final EnemyTypeEditorApi enemyTypeEditorApi;
	private final TowerTypesCollectionEditorApi towerTypesCollectionEditorApi;
	
	public TowerDefenseEditorApiImpl(LevelsCollectionEditorApi levelsCollectionEditorApi, EnemyTypeEditorApi enemyTypeEditorApi, TowerTypesCollectionEditorApi towerTypesCollectionEditorApi) {
		this.levelsCollectionEditorApi = levelsCollectionEditorApi;
		this.enemyTypeEditorApi = enemyTypeEditorApi;
		this.towerTypesCollectionEditorApi = towerTypesCollectionEditorApi;
	}
	
	
	@Override
	public LevelEditorApi createLevel(String id, int width, int height) {
		return levelsCollectionEditorApi.createLevel(id, width, height);
	}

	@Override
	public LevelEditorApi loadDraftLevel(String path) {
		return levelsCollectionEditorApi.loadDraftLevel(path);
	}

	@Override
	public LevelEditorApi loadExportableLevel(String path) {
		return this.levelsCollectionEditorApi.loadExportableLevel(path);
	}

	@Override
	public boolean removeLevel(String id) {
		return this.levelsCollectionEditorApi.removeLevel(id);
	}

	@Override
	public boolean saveDraftLevel(String id, String path) {
		return this.levelsCollectionEditorApi.saveDraftLevel(id, path);
	}

	@Override
	public boolean saveExportableLevel(String id, String path) {
		return this.levelsCollectionEditorApi.saveExportableLevel(id, path);
	}

	@Override
	public EditableLevel getEditableLevel(String id) {
		return this.levelsCollectionEditorApi.getEditableLevel(id);
	}

	@Override
	public List<EditableLevel> getAllEditableLevels() {
		return this.levelsCollectionEditorApi.getAllEditableLevels();
	}

	@Override
	public boolean createEnemyType(String id, int hp, double speed, int reward) {
		return this.enemyTypeEditorApi.createEnemyType(id, hp, speed, reward);
	}

	@Override
	public boolean loadDraftEnemyType(String path) {
		return this.enemyTypeEditorApi.loadDraftEnemyType(path);
	}

	@Override
	public boolean loadExportableEnemyType(String path) {
		return this.enemyTypeEditorApi.loadExportableEnemyType(path);
	}

	@Override
	public boolean removeEnemyType(String id) {
		return this.enemyTypeEditorApi.removeEnemyType(id);
	}

	@Override
	public List<EditableEnemyType> getAllEditableEnemyTypes() {
		return this.enemyTypeEditorApi.getAllEditableEnemyTypes();
	}

	@Override
	public EditableEnemyType getEditableEnemyType(String id) {
		return this.enemyTypeEditorApi.getEditableEnemyType(id);
	}

	@Override
	public boolean saveDraftEnemyType(String id, String path) {
		return this.enemyTypeEditorApi.saveDraftEnemyType(id, path);
	}

	@Override
	public boolean saveExportableEnemyType(String id, String path) {
		return this.enemyTypeEditorApi.saveExportableEnemyType(id, path);
	}

	@Override
	public TowerTypeEditorApi createTowerType(String id, String name) {
		return this.towerTypesCollectionEditorApi.createTowerType(id, name);
	}

	@Override
	public TowerTypeEditorApi loadDraftTowerType(String path) {
		return this.towerTypesCollectionEditorApi.loadDraftTowerType(path);
	}

	@Override
	public TowerTypeEditorApi loadExportableTowerType(String path) {
		return this.towerTypesCollectionEditorApi.loadExportableTowerType(path);
	}

	@Override
	public boolean removeTowerType(String id) {
		return this.towerTypesCollectionEditorApi.removeTowerType(id);
	}

	@Override
	public boolean saveDraftTowerType(String id, String path) {
		return this.towerTypesCollectionEditorApi.saveDraftTowerType(id, path);
	}

	@Override
	public boolean saveExportableTowerType(String id, String path) {
		return this.towerTypesCollectionEditorApi.saveDraftTowerType(id, path);
	}

	@Override
	public TowerTypeEditorApi getEditableTowerType(String id) {
		return this.towerTypesCollectionEditorApi.getEditableTowerType(id);
	}

	@Override
	public List<EditableTowerType> getAllEditableTowerTypes() {
		return this.towerTypesCollectionEditorApi.getAllEditableTowerTypes();
	}

}
