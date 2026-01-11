package com.towerdefense.leveleditor.http.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.towerdefense.editor.api.TowerDefenseEditorApi;
import com.towerdefense.leveleditor.http.api.DefaultApi;
import com.towerdefense.leveleditor.http.context.EditorContext;
import com.towerdefense.leveleditor.http.model.EnemiesConfig;
import com.towerdefense.leveleditor.http.model.EnemyFactory;
import com.towerdefense.leveleditor.http.model.EnemyPath;
import com.towerdefense.leveleditor.http.model.LevelConfig;
import com.towerdefense.leveleditor.http.model.LevelScenarioDefinition;
import com.towerdefense.leveleditor.http.model.PathsConfig;
import com.towerdefense.leveleditor.http.model.TowerType;
import com.towerdefense.leveleditor.http.model.TowersConfig;

import jakarta.validation.Valid;

@RestController
public class TowerDefenseEditorController implements DefaultApi {

	TowerDefenseEditorApi towerDefenseEditorApi;
	
	public TowerDefenseEditorController() {
		EditorContext context = new EditorContext();
		this.towerDefenseEditorApi = context.getTowerDefenseApi();
	}
	
	@Override
	public ResponseEntity<EnemyFactory> apiEnemiesIdGet(String id) {
		this.towerDefenseEditorApi.getEditableEnemyType(id);
		return null;
	}

	@Override
	public ResponseEntity<Map<String, EnemyFactory>> apiEnemiesPost(@Valid EnemiesConfig enemiesConfig) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<LevelScenarioDefinition> apiLevelsIdGet(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<LevelScenarioDefinition> apiLevelsPost(@Valid LevelConfig levelConfig) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<EnemyPath> apiPathsIdGet(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<List<EnemyPath>> apiPathsPost(@Valid PathsConfig pathsConfig) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<TowerType> apiTowersIdGet(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Map<String, TowerType>> apiTowersPost(@Valid TowersConfig towersConfig) {
		// TODO Auto-generated method stub
		return null;
	}

}
