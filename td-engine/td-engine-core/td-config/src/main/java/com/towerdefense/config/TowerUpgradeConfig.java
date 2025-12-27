package com.towerdefense.config;

/**
 * Represents the configuration for tower upgrades in the tower defense game.
 */
public class TowerUpgradeConfig {

	private int level;
	private int cost;
	private Integer damage; // nullable = pas de changement

	/** Constructor for TowerUpgradeConfig. */
	public TowerUpgradeConfig(int level, int cost, Integer damage) {
		super();
		this.level = level;
		this.cost = cost;
		this.damage = damage;
	}

	/** Get the level of the tower upgrade. */
	public int getLevel() {
		return level;
	}

	/** Get the cost of the tower upgrade. */
	public int getCost() {
		return cost;
	}

	/** Get the damage of the tower upgrade. */
	public Integer getDamage() {
		return damage;
	}

}
