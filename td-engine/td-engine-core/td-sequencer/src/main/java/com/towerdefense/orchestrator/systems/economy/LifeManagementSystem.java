package com.towerdefense.orchestrator.systems.economy;

import org.springframework.stereotype.Component;

import com.towerdefense.domain.GameState;
import com.towerdefense.domain.dynamik.enemy.Enemy;
import com.towerdefense.orchestrator.systems.core.GameSystem;
import com.towerdefense.orchestrator.systems.core.SystemPriority;

/**
 * Gère la perte de vies du joueur.
 * 
 * Responsabilités :
 * - Fait perdre des vies quand un ennemi atteint la fin
 * - (Future) Gère les bonus/malus de vies
 */
@Component
public class LifeManagementSystem implements GameSystem {
    
    @Override
    public void process(GameState state, int tick) {
        for (Enemy enemy : state.enemies()) {
            // Vérifie si l'ennemi a atteint la fin du chemin
            if (enemy.isAtEnd() && !enemy.health().isDead()) {
                state.player().loseLife();
            }
        }
    }
    
    @Override
    public SystemPriority priority() {
        return SystemPriority.LOW;
    }
}