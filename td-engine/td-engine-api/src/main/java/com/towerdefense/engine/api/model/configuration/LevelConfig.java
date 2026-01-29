package com.towerdefense.engine.api.model.configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the configuration for a rank in the tower defense game.
 */
public class LevelConfig {

	private String id;
	private List<AttackConfig> attacks;
	private int startingLives;
	private int startingMoney;
	
	private MapDimensions map;
	
	private List<PathConfig> paths;
	
    private List<TowerCapacityConfig> towerCapacities = new ArrayList<>();

	/** Get the number of lives for the rank. */
	public int getStartingLives() {
		return startingLives;
	}

	/** Set the number of lives for the rank. */
	public void setStartingLives(int startingLives) {
		this.startingLives = startingLives;
	}

	/** Get the starting money for the rank. */
	public int getStartingMoney() {
		return startingMoney;
	}

	/** Set the starting money for the rank. */
	public void setStartingMoney(int startingMoney) {
		this.startingMoney = startingMoney;
	}

	/** Get the unique identifier of the rank. */
	public String getId() {
		return id;
	}

	/** Get the list of attack configurations for the rank. */
	public List<AttackConfig> getAttacks() {
		return attacks;
	}

	/** Set the unique identifier of the rank. */
	public void setId(String id) {
		this.id = id;
	}

	/** Set the list of attack configurations for the rank. */
	public void setAttacks(List<AttackConfig> attacks) {
		this.attacks = attacks;
	}

	public List<TowerCapacityConfig> getTowerCapacities() {
		return towerCapacities;
	}

	public void setTowerCapacities(List<TowerCapacityConfig> towerCapacities) {
		this.towerCapacities = towerCapacities;
	}

	public MapDimensions getMap() {
		return map;
	}

	public void setMap(MapDimensions map) {
		this.map = map;
	}
	
	/** Get the list of path configurations. */
	public List<PathConfig> getPaths() {
	    return paths;
	}
	
	public void setPaths(List<PathConfig> paths) {
	    this.paths = paths;
	}
}
