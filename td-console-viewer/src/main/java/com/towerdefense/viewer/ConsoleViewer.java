package com.towerdefense.viewer;

import com.towerdefense.engine.api.GameStateObserver;
import com.towerdefense.engine.api.model.GameStateDTO;
import com.towerdefense.engine.api.model.LevelMapDTO;
import com.towerdefense.engine.api.model.events.EnemyHitEvent;
import com.towerdefense.engine.api.model.events.EnemyKilledEvent;
import com.towerdefense.engine.api.model.events.EnemyMovedEvent;
import com.towerdefense.engine.api.model.events.TowerShotEvent;

public class ConsoleViewer implements GameStateObserver {

    private final GameStateAsciiRenderer renderer;

    public ConsoleViewer(GameStateAsciiRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void onStateUpdated(GameStateDTO state, int tick) {
        System.out.println(renderer.print(state));
    }

	@Override
	public void onGameCreated(LevelMapDTO levelMap) {
		renderer.setMap(levelMap);
	}

	@Override
	public void onGameWon(GameStateDTO state) {
		System.out.println("Partie terminée avec succès");
	}

	@Override
	public void onGameLoose() {
		System.out.println("Partie écouhée");
	}

    @Override
    public void onTowerShot(TowerShotEvent event) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onEnemyHit(EnemyHitEvent event) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onEnemyKilled(EnemyKilledEvent event) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onEnemyMoved(EnemyMovedEvent event) {
        // TODO Auto-generated method stub
        
    }

}
