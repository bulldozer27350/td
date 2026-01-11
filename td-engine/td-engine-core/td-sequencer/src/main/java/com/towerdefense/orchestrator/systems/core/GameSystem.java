package com.towerdefense.orchestrator.systems.core;

import com.towerdefense.domain.GameState;

/**
 * Interface de base pour tous les systems du moteur de jeu.
 * 
 * Chaque system est responsable d'un aspect spécifique de la logique du jeu
 * et s'exécute dans un ordre défini par sa priorité.
 */
public interface GameSystem {
    
    /**
     * Exécute la logique du system pour le tick courant.
     * 
     * @param state l'état actuel du jeu (mutable)
     * @param tick le numéro du tick courant
     */
    void process(GameState state, int tick);
    
    /**
     * Priorité d'exécution du system.
     * Les systems avec une priorité plus basse s'exécutent en premier.
     * 
     * @return la priorité du system
     */
    default SystemPriority priority() {
        return SystemPriority.NORMAL;
    }
    
    /**
     * Indique si le system doit s'exécuter pour le tick courant.
     * 
     * @param state l'état actuel du jeu
     * @param tick le numéro du tick courant
     * @return true si le system doit s'exécuter
     */
    default boolean shouldProcess(GameState state, int tick) {
        return true;
    }
    
    /**
     * Nom du system pour le logging et le debugging.
     * 
     * @return le nom du system
     */
    default String name() {
        return this.getClass().getSimpleName();
    }
}