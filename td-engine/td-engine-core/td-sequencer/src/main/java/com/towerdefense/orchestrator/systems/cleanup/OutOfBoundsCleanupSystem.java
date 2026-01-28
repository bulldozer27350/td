package com.towerdefense.orchestrator.systems.cleanup;

import java.util.List;

import org.springframework.stereotype.Component;

import com.towerdefense.domain.GameState;
import com.towerdefense.engine.api.GameStateObserver;
import com.towerdefense.orchestrator.systems.core.GameSystem;
import com.towerdefense.orchestrator.systems.core.SystemPriority;

/**
 * Nettoie les projectiles orphelins ou hors limites.
 * 
 * Responsabilités :
 * - Supprime les projectiles dont la cible a disparu
 * - (Future) Supprime les projectiles hors de la carte
 */
@Component
public class OutOfBoundsCleanupSystem implements GameSystem {
    
    @Override
    public void process(GameState state, int tick, List<GameStateObserver> observers) {
        // Supprime les projectiles sans cible valide
        state.projectiles().removeIf(projectile -> 
            state.getEnemy(projectile.targetId()) == null
        );
    }
    
    @Override
    public SystemPriority priority() {
        return SystemPriority.LOWEST;
    }
}