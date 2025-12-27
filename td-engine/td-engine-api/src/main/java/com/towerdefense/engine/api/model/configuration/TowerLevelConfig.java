package com.towerdefense.engine.api.model.configuration;

/**
 * Represents the configuration for a tower level in the tower defense game.
 */
public class TowerLevelConfig {

	private int level;
	private int upgradeCost;
	private int sellValue;
	private double range;
	private int damage;
	private double reloadSeconds;
	private int buildTimeTicks;

	/** Get the level of the tower. */
	public int getLevel() {
		return level;
	}

	/** Get the upgrade cost of the tower. */
	public int getUpgradeCost() {
		return upgradeCost;
	}

	/** Get the sell value of the tower. */
	public int getSellValue() {
		return sellValue;
	}

	/** Get the range of the tower. */
	public double getRange() {
		return range;
	}

	/** Get the damage of the tower. */
	public int getDamage() {
		return damage;
	}

	/** Get the reload time in seconds of the tower. */
	public double getReloadSeconds() {
		return reloadSeconds;
	}

	/** Get the build time in ticks of the tower. */
	public int getBuildTimeTicks() {
		return buildTimeTicks;
	}
}
