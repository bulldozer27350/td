package com.towerdefense.orchestrator.systems.cleanup;

import java.util.List;

import org.springframework.stereotype.Component;

import com.towerdefense.domain.GameState;
import com.towerdefense.domain.dynamik.enemy.Enemy;
import com.towerdefense.engine.api.GameStateObserver;
import com.towerdefense.engine.api.model.events.EnemyKilledEvent;
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
    public void process(GameState state, int tick, List<GameStateObserver> observers) {
        // Supprime les ennemis morts ou arrivés
        for (Enemy target : state.enemies()) {
            if (target.health().isDead()) {
                System.out.println("[Tick " + tick + "]Sending event : Enemy " + target.id().value()
                        + " killed. Bounty: " + target.bounty());
                observers.forEach(observer -> observer
                        .onEnemyKilled(new EnemyKilledEvent(target.id().value(), target.bounty(), tick)));
                state.removeEnemy(target.id());
            }
            else if (target.isAtEnd()) {
                state.removeEnemy(target.id());
            }
        }
    }
    
    @Override
    public SystemPriority priority() {
        return SystemPriority.LOWEST;
    }
}