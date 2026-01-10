package com.towerdefense.exception;

/**
 * Exception levée quand un type d'ennemi est inconnu.
 */
public class UnknownEnemyTypeException extends InvalidConfigurationException {
    private final String enemyTypeId;
    
    public UnknownEnemyTypeException(String enemyTypeId) {
        super(
            "enemy type",
            String.format("Unknown enemy type: %s", enemyTypeId)
        );
        this.enemyTypeId = enemyTypeId;
    }
    
    public String getEnemyTypeId() { return enemyTypeId; }
}