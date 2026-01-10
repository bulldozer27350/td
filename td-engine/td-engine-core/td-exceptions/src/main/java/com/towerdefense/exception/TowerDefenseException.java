package com.towerdefense.exception;

/**
 * Exception de base pour toutes les erreurs métier du Tower Defense.
 * 
 * Contient un code d'erreur unique pour faciliter la gestion côté client.
 */
public abstract class TowerDefenseException extends RuntimeException {
    private final String errorCode;
    
    protected TowerDefenseException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    
    protected TowerDefenseException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}