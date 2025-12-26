package com.towerdefense.domain.intentions;

import com.towerdefense.domain.EntityId;

/**
 * Represents the intention of a player to sell a tower.
 *
 * @param playerId the ID of the player intending to sell the tower
 * @param towerId  the ID of the tower to be sold
 */
public record SellTowerIntention(EntityId playerId, EntityId towerId) {
}
