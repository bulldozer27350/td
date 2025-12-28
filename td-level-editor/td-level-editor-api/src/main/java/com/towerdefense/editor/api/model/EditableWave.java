package com.towerdefense.editor.api.model;

public class EditableWave {

    private int startTick;
    private int spawnInterval;
    private int count;
    private String enemyType;
    private String pathId;

    public EditableWave(int startTick, int spawnInterval, int count, String enemyType, String pathId) {
        this.startTick = startTick;
        this.spawnInterval = spawnInterval;
        this.count = count;
        this.enemyType = enemyType;
        this.pathId = pathId;
    }

    public int getStartTick() {
        return startTick;
    }

    public int getSpawnInterval() {
        return spawnInterval;
    }

    public int getCount() {
        return count;
    }

    public String getEnemyType() {
        return enemyType;
    }

    public String getPathId() {
        return pathId;
    }
}
