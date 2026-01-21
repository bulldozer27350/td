package com.towerdefense.progression.model;

import java.util.List;

//Modèle pour les niveaux
public record LevelData(
 String id,
 int startingMoney,
 int startingLives,
 LevelMap map,
 List<LevelPath> paths,
 List<Attack> attacks,
 List<TowerCapacity> towerCapacities
) {}
