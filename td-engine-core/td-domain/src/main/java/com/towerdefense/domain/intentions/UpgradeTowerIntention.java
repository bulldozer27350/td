package com.towerdefense.domain.intentions;

import com.towerdefense.domain.EntityId;

/**
 * Represents the intention of a player to upgrade a tower in the game.
 *
 * @param playerId The unique identifier of the player who wants to upgrade the tower.
 * @param towerId  The unique identifier of the tower to be upgraded.
 */
public record UpgradeTowerIntention(EntityId playerId, EntityId towerId) {
}
