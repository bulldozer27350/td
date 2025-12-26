package com.towerdefense.domain;

/**
 * Represents the health of a game entity in the tower defense game. It tracks
 * the current and maximum health values and provides methods to apply damage
 * and check if the entity is dead.
 */
public class Health {
	private final int max;
	private int current;

	/** Constructor to initialize health with a maximum value. */
	public Health(int max) {
		this.max = max;
		this.current = max;
	}

	/** Getters for current and maximum health values. */
	public int current() {
		return current;
	}

	/** Getters for current and maximum health values. */
	public int max() {
		return max;
	}

	/** Check if the entity is dead (current health is zero or below). */
	public boolean isDead() {
		return current <= 0;
	}

	/** Apply damage to the entity, reducing current health but not below zero. */
	public void applyDamage(int dmg) {
		current = Math.max(0, current - dmg);
	}
}