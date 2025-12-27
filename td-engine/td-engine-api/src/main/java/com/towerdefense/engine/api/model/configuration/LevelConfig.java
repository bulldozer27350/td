package com.towerdefense.engine.api.model.configuration;

import java.util.List;

/**
 * Represents the configuration for a level in the tower defense game.
 */
public class LevelConfig {

	private int id;
	private List<AttackConfig> attacks;
	private int startingLives;
	private int startingMoney;

	/** Get the number of lives for the level. */
	public int getStartingLives() {
		return startingLives;
	}

	/** Set the number of lives for the level. */
	public void setStartingLives(int startingLives) {
		this.startingLives = startingLives;
	}

	/** Get the starting money for the level. */
	public int getStartingMoney() {
		return startingMoney;
	}

	/** Set the starting money for the level. */
	public void setStartingMoney(int startingMoney) {
		this.startingMoney = startingMoney;
	}

	/** Get the unique identifier of the level. */
	public int getId() {
		return id;
	}

	/** Get the list of attack configurations for the level. */
	public List<AttackConfig> getAttacks() {
		return attacks;
	}

	/** Set the unique identifier of the level. */
	public void setId(int id) {
		this.id = id;
	}

	/** Set the list of attack configurations for the level. */
	public void setAttacks(List<AttackConfig> attacks) {
		this.attacks = attacks;
	}

}
