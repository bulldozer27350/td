package com.towerdefense.domain.statik.level;

public class TowerCapacityDefinition {
    
    private final String towerTypeId;
    private final int maxLevel;
    
    public TowerCapacityDefinition(String towerTypeId, int maxLevel) {
        this.towerTypeId = towerTypeId;
        this.maxLevel = maxLevel;
    }
    
    public String getTowerTypeId() { return towerTypeId; }
    public int getMaxLevel() { return maxLevel; }
}