package com.towerdefense.engine.api.model.events;

import com.towerdefense.engine.api.model.TowerDTO;

/**
 * Event triggered when a tower is placed in the game.
 *
 * @param tower The tower that has been placed.
 * @param tick  The game tick at which the tower was placed.
 */
public record TowerPlacedEvent(TowerDTO tower, int tick) {}
