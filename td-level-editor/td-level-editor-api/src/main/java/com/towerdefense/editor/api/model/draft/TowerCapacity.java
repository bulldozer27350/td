package com.towerdefense.editor.api.model.draft;

public class TowerCapacity {
    
    private String towerTypeId;
    private int maxRank;
    
    // For Jackson serialization
    public TowerCapacity() {}
    
    public TowerCapacity(String towerTypeId, int maxRank) {
        if (towerTypeId == null || towerTypeId.isBlank()) {
            throw new IllegalArgumentException("towerTypeId must not be null or blank");
        }
        if (maxRank < 1) {
            throw new IllegalArgumentException("maxRank must be >= 1");
        }
        this.towerTypeId = towerTypeId;
        this.maxRank = maxRank;
    }
    
    public String getTowerTypeId() { return towerTypeId; }
    public void setTowerTypeId(String towerTypeId) { this.towerTypeId = towerTypeId; }
    
    public int getMaxRank() { return maxRank; }
    public void setMaxRank(int maxRank) { this.maxRank = maxRank; }
    
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