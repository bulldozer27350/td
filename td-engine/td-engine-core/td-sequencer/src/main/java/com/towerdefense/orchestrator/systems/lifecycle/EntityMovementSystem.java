package com.towerdefense.orchestrator.systems.lifecycle;

import java.util.List;

import org.springframework.stereotype.Component;

import com.towerdefense.domain.GameState;
import com.towerdefense.domain.Position;
import com.towerdefense.domain.dynamik.enemy.Enemy;
import com.towerdefense.engine.api.GameStateObserver;
import com.towerdefense.engine.api.model.EnemyDTO;
import com.towerdefense.engine.api.model.PositionDTO;
import com.towerdefense.engine.api.model.events.EnemyMovedEvent;
import com.towerdefense.orchestrator.systems.core.GameSystem;
import com.towerdefense.orchestrator.systems.core.SystemPriority;

/**
 * Gère le déplacement de toutes les entités mobiles.
 * 
 * Responsabilités : - Déplace les ennemis le long de leurs chemins - Déplace
 * les projectiles vers leurs cibles
 */
@Component
public class EntityMovementSystem implements GameSystem {

    @Override
    public void process(GameState state, int tick, List<GameStateObserver> observers) {
        // Déplace tous les ennemis
        state.enemies().forEach(enemy -> {
            Position oldPosition = enemy.position();
            enemy.tick();
            Position newPosition = enemy.position();
            if (!oldPosition.equals(newPosition)) {
                System.out.println("[Tick " + tick + "]Sending event : Enemy " + enemy.id().value() + " moved from "
                        + oldPosition + " to " + newPosition);
                observers.forEach(observer -> observer.onEnemyMoved(new EnemyMovedEvent(toEnemyDTO(enemy), tick)));
            }
        });

        // Note : Le mouvement des projectiles est géré par ProjectileSystem
        // car il nécessite une logique plus complexe (targeting, collision)
    }

    private EnemyDTO toEnemyDTO(Enemy enemy) {
        return new EnemyDTO(enemy.id().value().toString(), new PositionDTO(enemy.position().x(), enemy.position().y()),
                enemy.health().max(), enemy.health().current(), !enemy.health().isDead());
    }

    @Override
    public SystemPriority priority() {
        return SystemPriority.HIGH;
    }
}