package com.towerdefense.editor.api.model.draft;

public record EditableTowerLevel(
    int level,
    int cost,
    double range,
    int damage,
    double reloadTime,
    int sellReward,
    int buildTimeTicks
) {}
