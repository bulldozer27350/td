package com.towerdefense.exception;

/**
 * Exception levée quand une tour ne peut pas être améliorée.
 */
public class TowerCannotBeUpgradedException extends TowerException {
    private final String reason;
    
    public TowerCannotBeUpgradedException(String towerId, String reason) {
        super(
            "TOWER_CANNOT_UPGRADE",
            String.format("Tower %s cannot be upgraded: %s", towerId, reason),
            towerId
        );
        this.reason = reason;
    }
    
    public String getReason() { return reason; }
}