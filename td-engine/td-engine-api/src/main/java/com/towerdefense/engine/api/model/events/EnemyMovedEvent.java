package com.towerdefense.engine.api.model.events;

import com.towerdefense.engine.api.model.EnemyDTO;

/**
 * Event representing the movement of an enemy in the game.
 *
 * @param enemyId     Unique identifier of the enemy that moved.
 * @param newPosition The new position of the enemy after movement.
 * @param tick        The game tick at which the movement occurred.
 */
public record EnemyMovedEvent(EnemyDTO enemy, int tick) {
}
