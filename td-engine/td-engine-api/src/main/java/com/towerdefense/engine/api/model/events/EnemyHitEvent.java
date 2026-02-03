package com.towerdefense.engine.api.model.events;

import java.util.UUID;

import com.towerdefense.engine.api.model.EnemyDTO;
import com.towerdefense.engine.api.model.PositionDTO;
import com.towerdefense.engine.api.model.TowerDTO;

public record EnemyHitEvent(
    EnemyDTO enemy,
    UUID projectileId,
    TowerDTO tower,
    int damage,
    int remainingHp,
    PositionDTO towerPosition,
    PositionDTO enemyPosition,
    int tick
) {}
