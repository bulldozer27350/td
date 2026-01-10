package com.towerdefense.exception;

/**
 * Exception levée quand un type de tour est inconnu.
 */
public class UnknownTowerTypeException extends InvalidConfigurationException {
    private final String towerTypeId;
    
    public UnknownTowerTypeException(String towerTypeId) {
        super(
            "tower type",
            String.format("Unknown tower type: %s", towerTypeId)
        );
        this.towerTypeId = towerTypeId;
    }
    
    public String getTowerTypeId() { return towerTypeId; }
}