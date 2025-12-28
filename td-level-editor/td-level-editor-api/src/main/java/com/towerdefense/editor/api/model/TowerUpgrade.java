package com.towerdefense.editor.api.model;

public record TowerUpgrade(
    int level,
    int costModifier,
    double rangeMultiplier,
    double damageMultiplier
) {}
