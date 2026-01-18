package com.towerdefense.progression.model;

public record Wave(
        String id,
        int startTick,
        int spawnInterval,
        int count,
        String enemyType,
        String pathId
    ) {}
