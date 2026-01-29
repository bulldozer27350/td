package com.towerdefense.viewer;

import com.towerdefense.engine.api.GameStateObserver;
import com.towerdefense.engine.api.model.GameStateDTO;
import com.towerdefense.engine.api.model.LevelMapDTO;
import com.towerdefense.engine.api.model.events.EnemyHitEvent;
import com.towerdefense.engine.api.model.events.EnemyKilledEvent;
import com.towerdefense.engine.api.model.events.EnemyMovedEvent;
import com.towerdefense.engine.api.model.events.TowerPlacedEvent;
import com.towerdefense.engine.api.model.events.TowerShotEvent;
import com.towerdefense.engine.api.model.events.TowerSoldEvent;
import com.towerdefense.engine.api.model.events.TowerUpgradedEvent;

public class FileViewer implements GameStateObserver {

    private final GameStateAsciiRenderer renderer;
	private String outputFile;

    public FileViewer(GameStateAsciiRenderer renderer, String outputFile) {
        this.renderer = renderer;
        this.outputFile = outputFile;
    }

    @Override
    public void onStateUpdated(GameStateDTO state, int tick) {
    	String ascii = renderer.print(state);
        AsciiFileWriter.write(outputFile, ascii, tick);
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

    @Override
    public void onTowerPlaced(TowerPlacedEvent event) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onTowerUpgraded(TowerUpgradedEvent event) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onTowerSold(TowerSoldEvent event) {
        // TODO Auto-generated method stub
        
    }
}
