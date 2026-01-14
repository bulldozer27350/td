package com.towerdefense.domain.statik.level;

import java.util.List;

/**
 * Represents the definition of a level scenario in a tower defense game.
 */
public class LevelScenarioDefinition {

	private final String id;
	private final List<AttackDefinition> attacks;
	private final int startingLives;
	private final int startingMoney;
	private final List<TowerCapacityDefinition> towerCapacities;

	/**
	 * Constructs a LevelScenarioDefinition with the specified id and list of attack
	 * definitions.
	 *
	 * @param id      the unique identifier for the level scenario
	 * @param attacks the list of attack definitions for this level scenario
	 */
	public LevelScenarioDefinition(String id, List<AttackDefinition> attacks, int startingLives, int startingMoney, List<TowerCapacityDefinition> towerCapacities) {
		this.id = id;
		this.attacks = List.copyOf(attacks);
		this.startingLives = startingLives;
		this.startingMoney = startingMoney;
	    this.towerCapacities = towerCapacities != null ? List.copyOf(towerCapacities) : List.of();
	}

	/**
	 * Gets the unique identifier for this level scenario.
	 * 
	 * @return the level scenario id
	 */
	public String getId() {
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
	
	public List<TowerCapacityDefinition> getTowerCapacities() {  // ✅ NOUVEAU
	    return towerCapacities;
	}
}
