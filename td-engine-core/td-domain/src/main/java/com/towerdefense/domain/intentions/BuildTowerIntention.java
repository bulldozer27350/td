package com.towerdefense.domain.intentions;

import com.towerdefense.domain.EntityId;
import com.towerdefense.domain.Position;
import com.towerdefense.domain.statik.tower.TowerType;

public record BuildTowerIntention(EntityId playerId, Position position, TowerType towerType) {}
