package com.towerdefense.progression.model;

public record EffectDefinition(
        String stat,
        double modifier,
        String type  // "ADD" ou "MULTIPLY"
    ) {}
