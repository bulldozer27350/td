package com.towerdefense.orchestrator.systems.combat;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.towerdefense.domain.EntityId;
import com.towerdefense.domain.GameState;
import com.towerdefense.domain.dynamik.enemy.Enemy;
import com.towerdefense.domain.projectile.Projectile;
import com.towerdefense.orchestrator.systems.core.GameSystem;
import com.towerdefense.orchestrator.systems.core.SystemPriority;

/**
 * Détecte et gère les collisions entre projectiles et ennemis.
 * 
 * Responsabilités :
 * - Détecte les collisions projectile/ennemi
 * - Applique les dégâts aux ennemis touchés
 * - Marque les projectiles pour destruction
 */
@Component
public class CollisionSystem implements GameSystem {
    
    @Override
    public void process(GameState state, int tick) {
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
                
                // Marque le projectile pour suppression
                projectilesToRemove.add(projectile.id());
            }
        }
        
        // Supprime les projectiles qui ont touché
        projectilesToRemove.forEach(state::removeProjectile);
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