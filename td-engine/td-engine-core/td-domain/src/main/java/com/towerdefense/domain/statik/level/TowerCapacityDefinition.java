package com.towerdefense.domain.statik.level;

public class TowerCapacityDefinition {
    
    private final String towerTypeId;
    private final int maxRank;
    
    public TowerCapacityDefinition(String towerTypeId, int maxRank) {
        this.towerTypeId = towerTypeId;
        this.maxRank = maxRank;
    }
    
    public String getTowerTypeId() { return towerTypeId; }
    public int getMaxRank() { return maxRank; }
}