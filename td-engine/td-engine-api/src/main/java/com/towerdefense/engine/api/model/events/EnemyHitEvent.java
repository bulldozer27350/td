package com.towerdefense.engine.api.model.events;

import java.util.UUID;

import com.towerdefense.engine.api.model.EnemyDTO;
import com.towerdefense.engine.api.model.PositionDTO;

public record EnemyHitEvent(
    EnemyDTO enemy,
    UUID projectileId,
//    UUID towerId,
    int damage,
    int remainingHp,
    PositionDTO towerPosition,
    PositionDTO enemyPosition,
    int tick
) {}
