package com.towerdefense.leveleditor.ui.context;

import com.towerdefense.editor.api.TowerDefenseEditorApi;
import com.towerdefense.editor.api.model.draft.EditableEnemyType;
import com.towerdefense.editor.api.model.draft.EditableLevel;
import com.towerdefense.editor.api.model.draft.EditableTowerType;
import com.towerdefense.editor.core.GenericIOServices;
import com.towerdefense.editor.core.exporter.EnemyTypeExportService;
import com.towerdefense.editor.core.exporter.LevelExportService;
import com.towerdefense.editor.core.exporter.TowerTypeExportService;
import com.towerdefense.editor.core.importer.EnemyTypeImportService;
import com.towerdefense.editor.core.importer.LevelImportService;
import com.towerdefense.editor.core.importer.TowerTypeImportService;
import com.towerdefense.editor.core.persistence.EnemyTypeJsonIO;
import com.towerdefense.editor.core.persistence.LevelJsonIO;
import com.towerdefense.editor.core.persistence.TowerTypeJsonIO;
import com.towerdefense.editor.core.validation.DefaultEnemyTypeValidator;
import com.towerdefense.editor.core.validation.DefaultLevelValidator;
import com.towerdefense.editor.core.validation.DefaultTowerTypeValidator;
import com.towerdefense.editor.implementation.EnemyTypeEditorApiImpl;
import com.towerdefense.editor.implementation.LevelsCollectionEditorApiImpl;
import com.towerdefense.editor.implementation.TowerDefenseEditorApiImpl;
import com.towerdefense.editor.implementation.TowerTypesCollectionEditorApiImpl;
import com.towerdefense.editor.itf.EnemyTypeEditorApi;
import com.towerdefense.editor.itf.LevelsCollectionEditorApi;
import com.towerdefense.editor.itf.TowerTypesCollectionEditorApi;

public class EditorContext {

	private final TowerDefenseEditorApi towerDefenseApi;

	public EditorContext() {
		GenericIOServices<EditableEnemyType> enemyTypeDraftIoServices = new GenericIOServices<EditableEnemyType>(
				EditableEnemyType.class);
		GenericIOServices<EditableLevel> levelDraftIoServices = new GenericIOServices<EditableLevel>(
				EditableLevel.class);
		GenericIOServices<EditableTowerType> towerTypeDraftIoServices = new GenericIOServices<EditableTowerType>(
				EditableTowerType.class);

		LevelExportService levelExportService = new LevelExportService(new DefaultLevelValidator());
		EnemyTypeExportService enemyTypeExportService = new EnemyTypeExportService(new DefaultEnemyTypeValidator());
		TowerTypeExportService towerTypeExportService = new TowerTypeExportService(new DefaultTowerTypeValidator());

		LevelImportService levelImportService = new LevelImportService();
		EnemyTypeImportService enemyTypeImportService = new EnemyTypeImportService();
		TowerTypeImportService towerTypeImportService = new TowerTypeImportService();

		LevelJsonIO levelJsonIo = new LevelJsonIO();
		EnemyTypeJsonIO enemyTypeJsonIo = new EnemyTypeJsonIO();
		TowerTypeJsonIO towerTypeJsonIo = new TowerTypeJsonIO();

		LevelsCollectionEditorApi levelsCollectionEditorApi = new LevelsCollectionEditorApiImpl(levelDraftIoServices,
				levelExportService, levelImportService, levelJsonIo);
		EnemyTypeEditorApi enemyTypeEditorApi = new EnemyTypeEditorApiImpl(enemyTypeDraftIoServices,
				enemyTypeExportService, enemyTypeImportService, enemyTypeJsonIo);
		TowerTypesCollectionEditorApi towerTypesCollectionEditorApi = new TowerTypesCollectionEditorApiImpl(
				towerTypeDraftIoServices, towerTypeExportService, towerTypeImportService, towerTypeJsonIo);
		
		this.towerDefenseApi = new TowerDefenseEditorApiImpl(levelsCollectionEditorApi, enemyTypeEditorApi,
				towerTypesCollectionEditorApi);
	}
	
	public TowerDefenseEditorApi getTowerDefenseApi() {
		return towerDefenseApi;
	}

}
