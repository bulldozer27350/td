package com.towerdefense.domain.statik.tower;

/**
 * Represents the definition of a tower level in the tower defense game.
 * Each level includes attributes such as upgrade cost, sell value, range,
 * damage, reload time, and build time.
 */
public record TowerLevelDefinition(
	    int level,
	    int upgradeCost,
	    int sellValue,
	    double range,
	    int damage,
	    double reloadSeconds,
	    int buildTimeTicks
	) {}
