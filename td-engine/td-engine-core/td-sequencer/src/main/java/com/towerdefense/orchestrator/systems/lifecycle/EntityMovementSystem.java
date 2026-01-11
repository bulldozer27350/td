package com.towerdefense.orchestrator.systems.lifecycle;

import org.springframework.stereotype.Component;

import com.towerdefense.domain.GameState;
import com.towerdefense.orchestrator.systems.core.GameSystem;
import com.towerdefense.orchestrator.systems.core.SystemPriority;

/**
 * Gère le déplacement de toutes les entités mobiles.
 * 
 * Responsabilités :
 * - Déplace les ennemis le long de leurs chemins
 * - Déplace les projectiles vers leurs cibles
 */
@Component
public class EntityMovementSystem implements GameSystem {
    
    @Override
    public void process(GameState state, int tick) {
        // Déplace tous les ennemis
        state.enemies().forEach(enemy -> enemy.tick());
        
        // Note : Le mouvement des projectiles est géré par ProjectileSystem
        // car il nécessite une logique plus complexe (targeting, collision)
    }
    
    @Override
    public SystemPriority priority() {
        return SystemPriority.HIGH;
    }
}