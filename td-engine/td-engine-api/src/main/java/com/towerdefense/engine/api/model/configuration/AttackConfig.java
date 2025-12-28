package com.towerdefense.engine.api.model.configuration;

import java.util.List;

/**
 * Represents the configuration for attack waves in the tower defense game.
 */
public class AttackConfig {

	private List<WaveConfig> waves;

	public AttackConfig() {
	}
	
	public AttackConfig(List<WaveConfig> waves) {
		this.waves = waves;
	}

	/** Get the list of wave configurations. */
	public List<WaveConfig> getWaves() {
		return waves;
	}
}
