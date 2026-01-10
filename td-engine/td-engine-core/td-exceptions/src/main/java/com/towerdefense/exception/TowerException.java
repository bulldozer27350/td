package com.towerdefense.exception;

/**
 * Classe de base pour toutes les exceptions liées aux tours.
 */
public abstract class TowerException extends TowerDefenseException {
    private final String towerId;
    
    protected TowerException(String errorCode, String message, String towerId) {
        super(errorCode, message);
        this.towerId = towerId;
    }
    
    public String getTowerId() { return towerId; }
}

