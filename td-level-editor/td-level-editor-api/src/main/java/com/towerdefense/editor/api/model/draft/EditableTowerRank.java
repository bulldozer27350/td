package com.towerdefense.editor.api.model.draft;

public record EditableTowerRank(
    int rank,
    int cost,
    double range,
    int damage,
    double reloadTime,
    int sellReward,
    int buildTimeTicks
) {}
