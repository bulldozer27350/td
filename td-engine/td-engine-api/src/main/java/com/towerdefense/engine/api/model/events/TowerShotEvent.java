package com.towerdefense.engine.api.model.events;

import java.util.UUID;

import com.towerdefense.engine.api.model.PositionDTO;

public record TowerShotEvent(
    UUID towerId,
    UUID targetEnemyId,
    UUID projectileId,
    PositionDTO towerPosition,
    PositionDTO targetPosition,
    int tick
) {}
