package com.towerdefense.exception;

/**
 * Exception levée quand une tour a atteint son niveau maximum.
 */
public class TowerMaxLevelReachedException extends TowerException {
    private final int currentLevel;
    
    public TowerMaxLevelReachedException(String towerId, int currentLevel) {
        super(
            "TOWER_MAX_LEVEL",
            String.format("Tower %s has reached max level %d", towerId, currentLevel),
            towerId
        );
        this.currentLevel = currentLevel;
    }
    
    public int getCurrentLevel() { return currentLevel; }
}