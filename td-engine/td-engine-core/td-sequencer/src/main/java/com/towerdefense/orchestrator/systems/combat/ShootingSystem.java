package com.towerdefense.orchestrator.systems.combat;

import org.springframework.stereotype.Component;

import com.towerdefense.domain.EntityId;
import com.towerdefense.domain.GameState;
import com.towerdefense.domain.dynamik.tower.Tower;
import com.towerdefense.domain.projectile.Projectile;
import com.towerdefense.orchestrator.systems.core.GameSystem;
import com.towerdefense.orchestrator.systems.core.SystemPriority;

/**
 * Gère le tir automatique des tours.
 * 
 * Responsabilités :
 * - Déclenche le tir des tours qui ont une cible
 * - Crée les projectiles correspondants
 * - Déclenche les cooldowns des tours
 */
@Component
public class ShootingSystem implements GameSystem {
    
    private final TargetingSystem targetingSystem;
    
    public ShootingSystem(TargetingSystem targetingSystem) {
        this.targetingSystem = targetingSystem;
    }
    
    @Override
    public void process(GameState state, int tick) {
        for (Tower tower : state.towers()) {
            if (!tower.isReady()) {
                continue;
            }
            
            // Vérifie si la tour a une cible
            targetingSystem.getTarget(tower.id()).ifPresent(targetId -> {
                // Déclenche le tir
                tower.triggerShot();
                
                // Crée le projectile
                Projectile projectile = createProjectile(tower, targetId);
                state.addProjectile(projectile);
            });
        }
    }
    
    /**
     * Crée un projectile tiré par une tour.
     */
    private Projectile createProjectile(Tower tower, EntityId targetId) {
        // TODO: Rendre configurable via TowerLevelDefinition
        double projectileSpeed = 8.0;
        
        return new Projectile(
            EntityId.random(),
            tower.position(),
            projectileSpeed,
            tower.damage(),
            targetId
        );
    }
    
    @Override
    public SystemPriority priority() {
        return SystemPriority.NORMAL;
    }
}