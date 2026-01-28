package com.towerdefense.orchestrator.systems.combat;

import java.util.List;

import org.springframework.stereotype.Component;

import com.towerdefense.domain.GameState;
import com.towerdefense.domain.dynamik.enemy.Enemy;
import com.towerdefense.domain.projectile.Projectile;
import com.towerdefense.engine.api.GameStateObserver;
import com.towerdefense.orchestrator.systems.core.GameSystem;
import com.towerdefense.orchestrator.systems.core.SystemPriority;

/**
 * Gère le déplacement des projectiles vers leurs cibles.
 * 
 * Responsabilités :
 * - Déplace chaque projectile vers sa cible
 * - Gère la disparition des cibles (projectile devient orphelin)
 */
@Component
public class ProjectileSystem implements GameSystem {
    
    @Override
    public void process(GameState state, int tick, List<GameStateObserver> observers) {
        for (Projectile projectile : state.projectiles()) {
            // Récupère la cible
            Enemy target = state.getEnemy(projectile.targetId());
            
            if (target == null) {
                // Cible disparue, le projectile sera nettoyé par CleanupSystem
                continue;
            }
            
            // Déplace le projectile vers la cible
            projectile.updateTowards(target.position());
        }
    }
    
    @Override
    public SystemPriority priority() {
        return SystemPriority.NORMAL;
    }
}