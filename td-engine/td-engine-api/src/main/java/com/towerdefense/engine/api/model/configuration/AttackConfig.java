package com.towerdefense.engine.api.model.configuration;

import java.util.List;

/**
 * Represents the configuration for attack waves in the tower defense game.
 */
public class AttackConfig {

	private List<WaveConfig> waves;
	private String id;

	// For serialization
	private AttackConfig() {
	}

	public AttackConfig(String id, List<WaveConfig> waves) {
		this.waves = waves;
		this.id = id;
	}

	/** Get the list of wave configurations. */
	public List<WaveConfig> getWaves() {
		return waves;
	}
	
	public String getId() {
		return this.id;
	}
}
