package com.towerdefense.exception;

/**
 * Exception levée quand le joueur n'a pas assez d'argent.
 */
public class InsufficientGoldException extends TowerDefenseException {
    private final int required;
    private final int available;
    
    public InsufficientGoldException(int required, int available) {
        super(
            "INSUFFICIENT_GOLD",
            String.format("Insufficient gold: required %d, available %d", required, available)
        );
        this.required = required;
        this.available = available;
    }
    
    public int getRequired() { return required; }
    public int getAvailable() { return available; }
}
