package com.towerdefense.engine.api.model;

public record LevelPlayerDTO(String id, int currentGold, int currentLives, LevelProgressDTO progress) {

}
