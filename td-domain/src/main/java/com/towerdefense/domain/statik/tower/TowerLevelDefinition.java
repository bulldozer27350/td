package com.towerdefense.domain.statik.tower;

public record TowerLevelDefinition(
	    int level,
	    int upgradeCost,
	    int sellValue,
	    double range,
	    int damage,
	    double reloadSeconds,
	    int buildTimeTicks
	) {}
