package com.towerdefense.engine.api.model.configuration;

/**
 * Represents the configuration for a tower rank in the tower defense game.
 */
public class TowerRankConfig {

	private int rank;
	private int upgradeCost;
	private int sellValue;
	private double range;
	private int damage;
	private double reloadSeconds;
	private int buildTimeTicks;

	// For serialization
	private TowerRankConfig() {
	}
	
	public TowerRankConfig(int rank, int upgradeCost, int sellValue, double range, int damage, double reloadSeconds,
			int buildTimeTicks) {
		this.rank = rank;
		this.upgradeCost = upgradeCost;
		this.sellValue = sellValue;
		this.range = range;
		this.damage = damage;
		this.reloadSeconds = reloadSeconds;
		this.buildTimeTicks = buildTimeTicks;
	}

	/** Get the rank of the tower. */
	public int getRank() {
		return rank;
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
