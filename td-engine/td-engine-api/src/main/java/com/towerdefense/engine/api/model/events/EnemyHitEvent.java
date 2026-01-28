package com.towerdefense.engine.api.model.events;

import java.util.UUID;

public record EnemyHitEvent(
    UUID enemyId,
    UUID projectileId,
//    UUID towerId,
    int damage,
    int remainingHp,
    int tick
) {}
