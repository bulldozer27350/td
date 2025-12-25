package com.towerdefense.config.dto;

public class WaveConfig {

    private int startTick;
    private int spawnInterval;
    private int count;
    private String enemyType;
    private String pathId;

    public int getStartTick() { return startTick; }
    public int getSpawnInterval() { return spawnInterval; }
    public int getCount() { return count; }
    public String getEnemyType() { return enemyType; }
    public String getPathId() { return pathId; }
}
