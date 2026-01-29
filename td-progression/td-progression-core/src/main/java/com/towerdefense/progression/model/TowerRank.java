package com.towerdefense.progression.model;

public record TowerRank(
        int rank,
        int upgradeCost,
        int sellValue,
        double range,
        int damage,
        double reloadSeconds,
        int buildTimeTicks
    ) {}
