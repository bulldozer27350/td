package com.towerdefense.domain.statik.tower;

/**
 * Represents the definition of a tower rank in the tower defense game.
 * Each rank includes attributes such as upgrade cost, sell value, range,
 * damage, reload time, and build time.
 */
public record TowerRankDefinition(
	    int rank,
	    int upgradeCost,
	    int sellValue,
	    double range,
	    int damage,
	    double reloadSeconds,
	    int buildTimeTicks
	) {}
