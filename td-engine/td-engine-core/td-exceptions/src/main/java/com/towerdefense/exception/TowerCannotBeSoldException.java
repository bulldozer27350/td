package com.towerdefense.exception;

/**
 * Exception levée quand une tour ne peut pas être vendue.
 */
public class TowerCannotBeSoldException extends TowerException {
    private final String reason;
    
    public TowerCannotBeSoldException(String towerId, String reason) {
        super(
            "TOWER_CANNOT_SELL",
            String.format("Tower %s cannot be sold: %s", towerId, reason),
            towerId
        );
        this.reason = reason;
    }
    
    public String getReason() { return reason; }
}