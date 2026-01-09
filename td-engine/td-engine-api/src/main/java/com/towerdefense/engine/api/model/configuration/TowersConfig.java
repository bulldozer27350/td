package com.towerdefense.engine.api.model.configuration;

import java.util.List;

/**
 * Represents the configuration for tower types in the tower defense game.
 */
public class TowersConfig {

    private List<TowerTypeConfig> towers;

    /** Get the list of tower type configurations. */
    public List<TowerTypeConfig> getTowers() {
        return towers;
    }

	public void setTowers(List<TowerTypeConfig> towers) {
		this.towers = towers;
	}
    
    
}
