package com.towerdefense.progression.model;

public record UpgradeDefinition(
        String id,
        String name,
        String towerTypeId,
        int towerLevel,
        int cost,
        EffectDefinition effect
    ) {}
