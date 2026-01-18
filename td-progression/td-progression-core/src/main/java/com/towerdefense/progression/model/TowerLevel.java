package com.towerdefense.progression.model;

public record TowerLevel(
        int level,
        int upgradeCost,
        int sellValue,
        double range,
        int damage,
        double reloadSeconds,
        int buildTimeTicks
    ) {}
