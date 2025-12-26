package com.towerdefense.domain.dynamik.level;

/**
 * Represents the definition of an enemy wave in a tower defense game. Contains
 * information about when the wave starts, the interval between enemy spawns,
 * the number of enemies, the type of enemies, and the path they will follow.
 */
public class WaveDefinition {

	private final int startTick;
	private final int spawnInterval;
	private final int count;
	private final String enemyTypeId;
	private final String pathId;

	/**
	 * Constructs a WaveDefinition with the specified parameters.
	 *
	 * @param startTick     The tick at which the wave starts.
	 * @param spawnInterval The interval between enemy spawns in ticks.
	 * @param count         The total number of enemies in the wave.
	 * @param enemyTypeId   The identifier for the type of enemy.
	 * @param pathId        The identifier for the path the enemies will follow.
	 */
	public WaveDefinition(int startTick, int spawnInterval, int count, String enemyTypeId, String pathId) {
		this.startTick = startTick;
		this.spawnInterval = spawnInterval;
		this.count = count;
		this.enemyTypeId = enemyTypeId;
		this.pathId = pathId;
	}

	/** Getters for the WaveDefinition properties */
	public int getStartTick() {
		return startTick;
	}

	/**
	 * Gets the interval between enemy spawns in ticks.
	 *
	 * @return The spawn interval in ticks.
	 */
	public int getSpawnInterval() {
		return spawnInterval;
	}

	/**
	 * Gets the total number of enemies in the wave.
	 * 
	 * @return The number of enemies.
	 */
	public int getCount() {
		return count;
	}

	/**
	 * Gets the identifier for the type of enemy.
	 * 
	 * @return The enemy type identifier.
	 */
	public String getEnemyTypeId() {
		return enemyTypeId;
	}

	/**
	 * Gets the identifier for the path the enemies will follow.
	 * 
	 * @return The path identifier.
	 */
	public String getPathId() {
		return pathId;
	}
}
