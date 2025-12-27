package com.towerdefense.engine.api.model;

public record TowerDTO(String id, PositionDTO position, TowerTypeEnum towerType, TowerStateEnum state) implements GameObject {

}
