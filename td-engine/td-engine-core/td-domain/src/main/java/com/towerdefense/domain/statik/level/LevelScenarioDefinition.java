package com.towerdefense.domain.statik.level;

import java.util.List;

/**
 * Represents the definition of a level scenario in a tower defense game.
 */
public class LevelScenarioDefinition {

	private final int id;
	private final List<AttackDefinition> attacks;
	private final int startingLives;
	private final int startingMoney;

	/**
	 * Constructs a LevelScenarioDefinition with the specified id and list of attack
	 * definitions.
	 *
	 * @param id      the unique identifier for the level scenario
	 * @param attacks the list of attack definitions for this level scenario
	 */
	public LevelScenarioDefinition(int id, List<AttackDefinition> attacks, int startingLives, int startingMoney) {
		this.id = id;
		this.attacks = List.copyOf(attacks);
		this.startingLives = startingLives;
		this.startingMoney = startingMoney;
	}

	/**
	 * Gets the unique identifier for this level scenario.
	 * 
	 * @return the level scenario id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the list of attack definitions for this level scenario.
	 * 
	 * @return the list of attack definitions
	 */
	public List<AttackDefinition> getAttacks() {
		return attacks;
	}

	/**
	 * Gets the starting lives for this level scenario.
	 * 
	 * @return the starting lives
	 */
	public int getStartingLives() {
		return startingLives;
	}

	/**
	 * Gets the starting money for this level scenario.
	 * 
	 * @return the starting money
	 */
	public int getStartingMoney() {
		return startingMoney;
	}
}
