package com.towerdefense.exception;

/**
 * Exception levée quand une tour a atteint son niveau maximum.
 */
public class TowerMaxRankReachedException extends TowerException {
    private final int currentRank;
    
    public TowerMaxRankReachedException(String towerId, int currentRank) {
        super(
            "TOWER_MAX_RANK",
            String.format("Tower %s has reached max rank %d", towerId, currentRank),
            towerId
        );
        this.currentRank = currentRank;
    }
    
    public int getCurrentRank() { return currentRank; }
}