package com.towerdefense.editor.api.model.exportable;

public record WaveDefinition(int startTick, int spawnInterval, int count, String enemyType, String pathId) {
}
