package com.towerdefense.progression.model;

public record UpgradeDefinition(
        String id,
        String name,
        String towerTypeId,
        String towerName,
        int towerRank,
        int cost,
        EffectDefinition effect
    ) {}
