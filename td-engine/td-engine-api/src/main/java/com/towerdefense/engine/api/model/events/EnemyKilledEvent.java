package com.towerdefense.engine.api.model.events;

import java.util.UUID;

public record EnemyKilledEvent(
    UUID enemyId,
//    UUID killerId, // Tour qui a porté le coup fatal
    int bountyAwarded,
    int tick
) {}