package com.towerdefense.engine.api.model.configuration;

public class TowerCapacityConfig {
    
    private String towerTypeId;
    private int maxLevel;
    
    // For Jackson
    public TowerCapacityConfig() {}
    
    public TowerCapacityConfig(String towerTypeId, int maxLevel) {
        this.towerTypeId = towerTypeId;
        this.maxLevel = maxLevel;
    }
    
    public String getTowerTypeId() { return towerTypeId; }
    public void setTowerTypeId(String towerTypeId) { this.towerTypeId = towerTypeId; }
    
    public int getMaxLevel() { return maxLevel; }
    public void setMaxLevel(int maxLevel) { this.maxLevel = maxLevel; }
}
