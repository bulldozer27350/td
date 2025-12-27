package com.towerdefense.domain.intentions;

import com.towerdefense.domain.EntityId;
import com.towerdefense.domain.Position;
import com.towerdefense.domain.statik.tower.TowerType;

/**
 * Represents the intention of a player to build a tower at a specific position.
 *
 * @param playerId  the ID of the player intending to build the tower
 * @param position  the position where the tower is to be built
 * @param towerType the type of tower to be built
 */
public record BuildTowerIntention(EntityId playerId, Position position, TowerType towerType) {}
