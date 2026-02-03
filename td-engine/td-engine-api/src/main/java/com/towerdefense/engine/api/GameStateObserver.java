package com.towerdefense.engine.api;

import com.towerdefense.engine.api.model.GameStateDTO;
import com.towerdefense.engine.api.model.LevelMapDTO;
import com.towerdefense.engine.api.model.events.EnemyHitEvent;
import com.towerdefense.engine.api.model.events.EnemyKilledEvent;
import com.towerdefense.engine.api.model.events.EnemyMovedEvent;
import com.towerdefense.engine.api.model.events.TowerPlacedEvent;
import com.towerdefense.engine.api.model.events.TowerShotEvent;
import com.towerdefense.engine.api.model.events.TowerSoldEvent;
import com.towerdefense.engine.api.model.events.TowerUpgradedEvent;

public interface GameStateObserver {

	void onStateUpdated(GameStateDTO state, int tick);
	
	void onGameCreated(LevelMapDTO levelMap);

	void onGameWon(GameStateDTO state);

	void onGameLoose(GameStateDTO state);
	
	void onTowerShot(TowerShotEvent event);
	
    void onEnemyHit(EnemyHitEvent event);
    
    void onEnemyKilled(EnemyKilledEvent event);
    
    void onEnemyMoved(EnemyMovedEvent event);
    
    void onTowerPlaced(TowerPlacedEvent event);
    
    void onTowerUpgraded(TowerUpgradedEvent event);
    
    void onTowerSold(TowerSoldEvent event);

}
