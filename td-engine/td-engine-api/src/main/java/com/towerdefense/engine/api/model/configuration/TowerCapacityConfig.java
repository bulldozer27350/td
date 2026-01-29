package com.towerdefense.engine.api.model.configuration;

public class TowerCapacityConfig {
    
    private String towerTypeId;
    private int maxRank;
    
    // For Jackson
    public TowerCapacityConfig() {}
    
    public TowerCapacityConfig(String towerTypeId, int maxRank) {
        this.towerTypeId = towerTypeId;
        this.maxRank = maxRank;
    }
    
    public String getTowerTypeId() { return towerTypeId; }
    public void setTowerTypeId(String towerTypeId) { this.towerTypeId = towerTypeId; }
    
    public int getMaxRank() { return maxRank; }
    public void setMaxRank(int maxRank) { this.maxRank = maxRank; }
}
