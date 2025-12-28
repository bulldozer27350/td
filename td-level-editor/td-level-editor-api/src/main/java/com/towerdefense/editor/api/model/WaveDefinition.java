package com.towerdefense.editor.api.model;

public record WaveDefinition(int startTick, int spawnInterval, int count, String enemyType, String pathId) {
}
