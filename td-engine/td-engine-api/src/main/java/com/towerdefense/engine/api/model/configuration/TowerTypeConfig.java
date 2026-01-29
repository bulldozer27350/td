package com.towerdefense.engine.api.model.configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the configuration for a tower type in the tower defense game.
 */
public class TowerTypeConfig {

    private String id;
    private String name;
    private List<TowerRankConfig> ranks = new ArrayList<TowerRankConfig>();

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

	/** Get the list of tower rank configurations. */
    public List<TowerRankConfig> getRanks() { return ranks; }

	public void setRanks(List<TowerRankConfig> ranks) {
		this.ranks = ranks;
	}
    
    
}

