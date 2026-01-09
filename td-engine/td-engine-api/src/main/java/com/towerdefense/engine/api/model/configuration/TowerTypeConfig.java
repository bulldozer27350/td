package com.towerdefense.engine.api.model.configuration;

import java.util.List;

/**
 * Represents the configuration for a tower type in the tower defense game.
 */
public class TowerTypeConfig {

    private String id;
    private String name;
    private List<TowerLevelConfig> levels;

    /** Get the unique identifier of the tower type. */
    public String getId() { return id; }
    
    public void setId(String id) {
		this.id = id;
	}
    
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/** Get the list of tower level configurations. */
    public List<TowerLevelConfig> getLevels() { return levels; }

	public void setLevels(List<TowerLevelConfig> levels) {
		this.levels = levels;
	}
    
    
}

