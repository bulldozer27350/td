package com.towerdefense.progression.model;

import java.util.List;

// Modèle pour les tours
public record TowerTypeData(
    String id,
    String name,
    List<TowerLevel> levels
) {}
