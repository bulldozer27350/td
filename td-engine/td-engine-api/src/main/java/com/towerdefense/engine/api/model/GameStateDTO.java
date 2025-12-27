package com.towerdefense.engine.api.model;

import java.util.List;

public record GameStateDTO(LevelPlayerDTO player, List<TowerDTO> towers, List<EnemyDTO> enemies, List<ProjectileDTO> projectiles) {

}
