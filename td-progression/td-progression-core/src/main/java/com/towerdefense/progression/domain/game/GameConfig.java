package com.towerdefense.progression.domain.game;

import java.util.List;

import com.towerdefense.progression.model.EnemyTypeData;
import com.towerdefense.progression.model.LevelData;
import com.towerdefense.progression.model.TowerTypeData;

public record GameConfig(LevelData level,
         List<TowerTypeData> towers,
         List<EnemyTypeData> enemies) {

}
