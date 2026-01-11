package com.towerdefense.orchestrator.systems.core;

/**
 * Définit l'ordre d'exécution des systems.
 * 
 * Les systems s'exécutent dans l'ordre suivant :
 * HIGHEST → HIGH → NORMAL → LOW → LOWEST
 */
public enum SystemPriority {
    /**
     * S'exécute en premier (ex: TimeManagement, LevelProgression)
     */
    HIGHEST(0),
    
    /**
     * S'exécute tôt (ex: Movement, Targeting)
     */
    HIGH(100),
    
    /**
     * Ordre par défaut (ex: Shooting, Projectiles)
     */
    NORMAL(200),
    
    /**
     * S'exécute tard (ex: Collision, Rewards)
     */
    LOW(300),
    
    /**
     * S'exécute en dernier (ex: Cleanup, Notifications)
     */
    LOWEST(400);
    
    private final int value;
    
    SystemPriority(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    
    public boolean isBefore(SystemPriority other) {
        return this.value < other.value;
    }
}