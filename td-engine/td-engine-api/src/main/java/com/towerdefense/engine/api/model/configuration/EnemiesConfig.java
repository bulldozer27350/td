package com.towerdefense.engine.api.model.configuration;

import java.util.List;

/**
 * Represents the configuration for enemy types in the tower defense game.
 */
public class EnemiesConfig {

    private List<EnemyTypeConfig> enemies;

    /** Get the list of enemy type configurations. */
    public List<EnemyTypeConfig> getEnemies() {
        return enemies;
    }

	public void setEnemies(List<EnemyTypeConfig> enemies) {
		this.enemies = enemies;
	}
    
}
