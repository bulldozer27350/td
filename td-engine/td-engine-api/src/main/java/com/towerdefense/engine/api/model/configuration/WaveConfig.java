package com.towerdefense.engine.api.model.configuration;

/**
 * Represents the configuration for a single wave of enemies in the tower
 * defense game.
 */
public class WaveConfig {

	private int startTick;
	private int spawnInterval;
	private int count;
	private String enemyType;
	private String pathId;

	public WaveConfig() {
	}
	
	/** Constructor to initialize all fields of the wave configuration. */
	public WaveConfig(int startTick, int spawnInterval, int count, String enemyType, String pathId) {
		this.startTick = startTick;
		this.spawnInterval = spawnInterval;
		this.count = count;
		this.enemyType = enemyType;
		this.pathId = pathId;
	}
	
	/** Get the tick at which the wave starts. */
	public int getStartTick() {
		return startTick;
	}

	/** Get the interval between enemy spawns in ticks. */
	public int getSpawnInterval() {
		return spawnInterval;
	}

	/** Get the total number of enemies in the wave. */
	public int getCount() {
		return count;
	}

	/** Get the type of enemy in the wave. */
	public String getEnemyType() {
		return enemyType;
	}

	/** Get the identifier of the path the enemies will follow. */
	public String getPathId() {
		return pathId;
	}
}
