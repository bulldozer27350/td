package com.towerdefense.domain.player;

import com.towerdefense.domain.EntityId;

/**
 * Represents the state of a player in the tower defense game, including their
 * gold and lives.
 */
public class PlayerState {

	private final EntityId id;
	private int gold;
	private int lives;

	/** Constructor to initialize player state with given id, gold, and lives. */
	public PlayerState(EntityId id, int gold, int lives) {
		this.id = id;
		this.gold = gold;
		this.lives = lives;
	}

	/** Getters for player state attributes */
	public EntityId id() {
		return id;
	}

	/** Getters for player state attributes */
	public int gold() {
		return gold;
	}

	/** Getters for player state attributes */
	public int lives() {
		return lives;
	}

	/** Check if the player can afford a certain cost */
	public boolean canAfford(int cost) {
		return gold >= cost;
	}

	/** Spend a certain amount of gold */
	public void spendGold(int amount) {
		gold -= amount;
	}

	/** Earn a certain amount of gold */
	public void earnGold(int amount) {
		gold += amount;
	}

	/** Lose a life */
	public void loseLife() {
		lives--;
	}

	/** Check if the player is still alive */
	public boolean isAlive() {
		return lives > 0;
	}
}
