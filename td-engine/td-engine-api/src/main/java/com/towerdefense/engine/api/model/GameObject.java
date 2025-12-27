package com.towerdefense.engine.api.model;

/**
 * Represents a game object with a position in the tower defense game.
 */
public interface GameObject {
	/** Get the unique identifier of the game object. */
    PositionDTO position();
}
