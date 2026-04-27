package com.towerdefense.orchestrator.systems.combat;

import java.util.List;

import org.springframework.stereotype.Component;

import com.towerdefense.domain.EntityId;
import com.towerdefense.domain.GameState;
import com.towerdefense.domain.dynamik.enemy.Enemy;
import com.towerdefense.domain.dynamik.tower.Tower;
import com.towerdefense.domain.projectile.Projectile;
import com.towerdefense.engine.api.GameStateObserver;
import com.towerdefense.engine.api.model.PositionDTO;
import com.towerdefense.engine.api.model.events.TowerShotEvent;
import com.towerdefense.orchestrator.systems.core.GameSystem;
import com.towerdefense.orchestrator.systems.core.SystemPriority;

/**
 * Gère le tir automatique des tours.
 * 
 * Responsabilités : - Déclenche le tir des tours qui ont une cible - Crée les
 * projectiles correspondants - Déclenche les cooldowns des tours
 */
@Component
public class ShootingSystem implements GameSystem {

    private final TargetingSystem targetingSystem;

    public ShootingSystem(TargetingSystem targetingSystem) {
        this.targetingSystem = targetingSystem;
    }

    @Override
    public void process(GameState state, int tick, List<GameStateObserver> observers) {
        for (Tower tower : state.towers()) {
            if (!tower.isReady()) {
                continue;
            }

            // Vérifie si la tour a une cible
            targetingSystem.findTarget(tower, state).ifPresent(target -> {
                // Déclenche le tir
                tower.triggerShot();

                // Crée le projectile
                Projectile projectile = createProjectile(tower, target.id());
                state.addProjectile(projectile);

                System.out.println("[Tick " + tick + "]Sending event : Tower " + tower.id().value() + " shot toward Enemy "
                        + target.id().value() + " with Projectile " + projectile.id().value());
                observers.forEach(
                        observer -> observer.onTowerShot(this.createTowerShotEvent(tick, tower, target, projectile)));
            });
        }
    }

    private TowerShotEvent createTowerShotEvent(int tick, Tower tower, Enemy target, Projectile projectile) {
        return new TowerShotEvent(tower.id().value(), target.id().value(),
                projectile.id().value(), new PositionDTO(tower.position().x(), tower.position().y()),
                new PositionDTO(target.position().x(), target.position().y()), tick);
    }

    /**
     * Crée un projectile tiré par une tour.
     */
    private Projectile createProjectile(Tower tower, EntityId targetId) {
        // TODO: Rendre configurable via TowerRankDefinition
        double projectileSpeed = 16.0;

        return new Projectile(EntityId.random(), tower.position(), projectileSpeed, tower.damage(), targetId, tower.id());
    }

    @Override
    public SystemPriority priority() {
        return SystemPriority.NORMAL;
    }
}