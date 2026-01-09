package com.towerdefense.config;

import java.util.Map;

import com.towerdefense.config.registry.EnemyFactoryRegistry;
import com.towerdefense.config.registry.TowerTypeRegistry;
import com.towerdefense.domain.map.EnemyPath;
import com.towerdefense.domain.statik.level.LevelScenarioDefinition;

public class EngineContext {

    private TowerTypeRegistry towerTypeRegistry;
    private EnemyFactoryRegistry enemyFactoryRegistry;
    private LevelScenarioDefinition levelDefinition;
    private Map<String, EnemyPath> enemyPaths;

    public void setTowerTypeRegistry(TowerTypeRegistry registry) {
        this.towerTypeRegistry = registry;
    }

    public TowerTypeRegistry towerTypeRegistry() {
        return towerTypeRegistry;
    }

	public void setEnemyFactoryRegistry(EnemyFactoryRegistry registry) {
		this.enemyFactoryRegistry = registry;
	}
	
	public EnemyFactoryRegistry enemyFactoryRegistry() {
		return enemyFactoryRegistry;
	}

	public void setLevelScenarioDefinition(LevelScenarioDefinition scenarioDefinition) {
		this.levelDefinition = scenarioDefinition;
	}
	
	public LevelScenarioDefinition levelScenarioDefinition() {
		return levelDefinition;
	}

	public void setEnemyPaths(Map<String, EnemyPath> path) {
		this.enemyPaths = path;
	}
	
	public Map<String, EnemyPath> enemyPaths() {
		return enemyPaths;
	}
}

