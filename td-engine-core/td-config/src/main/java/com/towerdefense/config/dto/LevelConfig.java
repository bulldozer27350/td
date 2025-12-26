package com.towerdefense.config.dto;

import java.util.List;

/**
 * Represents the configuration for a level in the tower defense game.
 */
public class LevelConfig {

	private int id;
	private List<AttackConfig> attacks;

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
