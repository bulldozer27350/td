package com.towerdefense.progression.model;

import java.util.List;

//Modèle pour les niveaux
public record LevelData(
 Metadata metadata,
 Map map,
 List<Path> paths,
 List<Attack> attacks,
 List<TowerCapacity> towerCapacities
) {}
