package com.towerdefense.orchestrator.systems.cleanup;

import org.springframework.stereotype.Component;

import com.towerdefense.domain.GameState;
import com.towerdefense.orchestrator.systems.core.GameSystem;
import com.towerdefense.orchestrator.systems.core.SystemPriority;

/**
 * Nettoie les entités mortes ou invalides.
 * 
 * Responsabilités :
 * - Supprime les ennemis morts
 * - Supprime les ennemis qui ont atteint la fin
 * - (Future) Gère les animations de mort
 */
@Component
public class DeadEntityCleanupSystem implements GameSystem {
    
    @Override
    public void process(GameState state, int tick) {
        // Supprime les ennemis morts ou arrivés
        state.enemies().removeIf(enemy -> 
            enemy.health().isDead() || enemy.isAtEnd()
        );
    }
    
    @Override
    public SystemPriority priority() {
        return SystemPriority.LOWEST;
    }
}