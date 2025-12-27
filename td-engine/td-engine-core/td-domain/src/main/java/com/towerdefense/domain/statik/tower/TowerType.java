package com.towerdefense.domain.statik.tower;

import java.util.List;

/**
 * Represents a type of tower in the tower defense game. Each tower type has a
 * name and multiple levels defined by TowerLevelDefinition.
 */
public class TowerType {

	private final String name;
	private final List<TowerLevelDefinition> levels;

	/** Constructor to initialize tower type with given name and levels. */
	public TowerType(String name, List<TowerLevelDefinition> levels) {
		this.name = name;
		this.levels = List.copyOf(levels);
	}

	/** Get the definition of a specific tower level. */
	public TowerLevelDefinition level(int level) {
		return levels.get(level - 1);
	}

	/** Get the maximum level available for this tower type. */
	public int maxLevel() {
		return levels.size();
	}

	/** Get the name of the tower type. */
	public String name() {
		return name;
	}
}
