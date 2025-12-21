package com.towerdefense.domain.dynamik.level;

public class WaveDefinition {

    private final int startTick;
    private final int spawnInterval;
    private final int count;
    private final String enemyTypeId;
    private final String pathId;

    public WaveDefinition(
        int startTick,
        int spawnInterval,
        int count,
        String enemyTypeId,
        String pathId
    ) {
        this.startTick = startTick;
        this.spawnInterval = spawnInterval;
        this.count = count;
        this.enemyTypeId = enemyTypeId;
        this.pathId = pathId;
    }

    public int getStartTick() { return startTick; }
    public int getSpawnInterval() { return spawnInterval; }
    public int getCount() { return count; }
    public String getEnemyTypeId() { return enemyTypeId; }
    public String getPathId() { return pathId; }
}
