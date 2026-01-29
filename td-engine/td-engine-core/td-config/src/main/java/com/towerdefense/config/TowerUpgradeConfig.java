package com.towerdefense.config;

/**
 * Represents the configuration for tower upgrades in the tower defense game.
 */
public class TowerUpgradeConfig {

	private int rank;
	private int cost;
	private Integer damage; // nullable = pas de changement

	/** Constructor for TowerUpgradeConfig. */
	public TowerUpgradeConfig(int rank, int cost, Integer damage) {
		super();
		this.rank = rank;
		this.cost = cost;
		this.damage = damage;
	}

	/** Get the rank of the tower upgrade. */
	public int getLevel() {
		return rank;
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
