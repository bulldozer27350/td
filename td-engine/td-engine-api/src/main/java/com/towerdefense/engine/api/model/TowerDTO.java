package com.towerdefense.engine.api.model;

public record TowerDTO(String id, PositionDTO position, String towerType, TowerStateEnum state) implements GameObject {

}
