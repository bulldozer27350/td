package com.towerdefense.editor.api.model.draft;

public class TowerCapacity {
    
    private String towerTypeId;
    private int maxLevel;
    
    // For Jackson serialization
    public TowerCapacity() {}
    
    public TowerCapacity(String towerTypeId, int maxLevel) {
        if (towerTypeId == null || towerTypeId.isBlank()) {
            throw new IllegalArgumentException("towerTypeId must not be null or blank");
        }
        if (maxLevel < 1) {
            throw new IllegalArgumentException("maxLevel must be >= 1");
        }
        this.towerTypeId = towerTypeId;
        this.maxLevel = maxLevel;
    }
    
    public String getTowerTypeId() { return towerTypeId; }
    public void setTowerTypeId(String towerTypeId) { this.towerTypeId = towerTypeId; }
    
    public int getMaxLevel() { return maxLevel; }
    public void setMaxLevel(int maxLevel) { this.maxLevel = maxLevel; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TowerCapacity that = (TowerCapacity) o;
        return towerTypeId.equals(that.towerTypeId);
    }
    
    @Override
    public int hashCode() {
        return towerTypeId.hashCode();
    }
}