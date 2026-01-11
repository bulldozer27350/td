package com.towerdefense.orchestrator.systems.economy;

import org.springframework.stereotype.Component;

import com.towerdefense.domain.GameState;
import com.towerdefense.domain.dynamik.enemy.Enemy;
import com.towerdefense.orchestrator.systems.core.GameSystem;
import com.towerdefense.orchestrator.systems.core.SystemPriority;

/**
 * Gère les récompenses en or pour les ennemis tués.
 * 
 * Responsabilités :
 * - Accorde l'or pour chaque ennemi mort
 * - (Future) Gère les bonus/malus d'or
 */
@Component
public class RewardSystem implements GameSystem {
    
    @Override
    public void process(GameState state, int tick) {
        for (Enemy enemy : state.enemies()) {
            if (enemy.health().isDead()) {
                // Accorde l'or du bounty au joueur
                state.player().earnGold(enemy.bounty());
            }
        }
    }
    
    @Override
    public SystemPriority priority() {
        return SystemPriority.LOW;
    }
}