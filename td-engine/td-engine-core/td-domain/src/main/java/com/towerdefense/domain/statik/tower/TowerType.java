package com.towerdefense.domain.statik.tower;

import java.util.List;

/**
 * Represents a type of tower in the tower defense game. Each tower type has a
 * name and multiple ranks defined by TowerRankDefinition.
 */
public class TowerType {

	private final String name;
	private final List<TowerRankDefinition> ranks;

	/** Constructor to initialize tower type with given name and ranks. */
	public TowerType(String name, List<TowerRankDefinition> levels) {
		this.name = name;
		this.ranks = List.copyOf(levels);
	}

	/** Get the definition of a specific tower rank. */
	public TowerRankDefinition rank(int rank) {
		return ranks.get(rank - 1);
	}

	/** Get the maximum rank available for this tower type. */
	public int maxRank() {
		return ranks.size();
	}

	/** Get the name of the tower type. */
	public String name() {
		return name;
	}
}
