package com.towerdefense.engine.api.model;

public record EnemyDTO(String id, PositionDTO position, int maxHp, int currentHp, boolean isAlive) implements GameObject {

}
