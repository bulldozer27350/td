package com.towerdefense.domain;

/**
 * Represents a game object with a position in the tower defense game.
 */
public interface GameObject {
	/** Get the unique identifier of the game object. */
    Position position();
}
