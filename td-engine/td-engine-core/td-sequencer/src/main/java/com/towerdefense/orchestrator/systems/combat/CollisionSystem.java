package com.towerdefense.orchestrator.systems.combat;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.towerdefense.domain.EntityId;
import com.towerdefense.domain.GameState;
import com.towerdefense.domain.dynamik.enemy.Enemy;
import com.towerdefense.domain.dynamik.tower.Tower;
import com.towerdefense.domain.projectile.Projectile;
import com.towerdefense.engine.api.GameStateObserver;
import com.towerdefense.engine.api.model.EnemyDTO;
import com.towerdefense.engine.api.model.PositionDTO;
import com.towerdefense.engine.api.model.events.EnemyHitEvent;
import com.towerdefense.engine.api.model.events.EnemyKilledEvent;
import com.towerdefense.orchestrator.systems.core.GameSystem;
import com.towerdefense.orchestrator.systems.core.SystemPriority;

/**
 * Détecte et gère les collisions entre projectiles et ennemis.
 * 
 * Responsabilités : - Détecte les collisions projectile/ennemi - Applique les
 * dégâts aux ennemis touchés - Marque les projectiles pour destruction
 */
@Component
public class CollisionSystem implements GameSystem {

    @Override
    public void process(GameState state, int tick, List<GameStateObserver> observers) {
        List<EntityId> projectilesToRemove = new ArrayList<>();

        for (Projectile projectile : state.projectiles()) {
            Enemy target = state.getEnemy(projectile.targetId());

            if (target == null) {
                // Cible disparue, marque pour nettoyage
                projectilesToRemove.add(projectile.id());
                continue;
            }

            // Vérifie la collision (position identique)
            if (hasCollided(projectile, target)) {
                // Applique les dégâts
                target.health().applyDamage(projectile.damage());

                Tower tower = state.getTower(projectile.towerId());
                System.out.println("[Tick " + tick + "]Sending event : Projectile " + projectile.id().value()
                        + " hit Enemy " + target.id().value() + " for " + projectile.damage()
                        + " damage. Enemy health: " + target.health().current());
                observers.forEach(observer -> observer
                        .onEnemyHit(new EnemyHitEvent(toEnemyDTO(target), projectile.id().value(), projectile.damage(),
                                target.health().current(), new PositionDTO(tower.position().x(), tower.position().y()),
                                new PositionDTO(target.position().x(), target.position().y()), tick)));

                if (target.health().isDead()) {
                    System.out.println("[Tick " + tick + "]Sending event : Enemy " + target.id().value()
                            + " killed. Bounty: " + target.bounty());
                    observers.forEach(observer -> observer
                            .onEnemyKilled(new EnemyKilledEvent(target.id().value(), target.bounty(), tick)));
                    state.removeEnemy(target.id());
                }

                // Marque le projectile pour suppression
                projectilesToRemove.add(projectile.id());
            }
        }

        // Supprime les projectiles qui ont touché
        projectilesToRemove.forEach(state::removeProjectile);
    }

    private EnemyDTO toEnemyDTO(Enemy enemy) {
        return new EnemyDTO(enemy.id().value().toString(), new PositionDTO(enemy.position().x(), enemy.position().y()),
                enemy.health().max(), enemy.health().current(), !enemy.health().isDead());
    }

    /**
     * Vérifie si un projectile a touché sa cible.
     */
    private boolean hasCollided(Projectile projectile, Enemy target) {
        return projectile.position().equals(target.position());
    }

    @Override
    public SystemPriority priority() {
        return SystemPriority.LOW;
    }
}