package com.towerdefense.orchestrator.systems.combat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.towerdefense.domain.EntityId;
import com.towerdefense.domain.GameState;
import com.towerdefense.domain.dynamik.enemy.Enemy;
import com.towerdefense.domain.dynamik.tower.Tower;
import com.towerdefense.engine.api.GameStateObserver;
import com.towerdefense.orchestrator.systems.core.GameSystem;
import com.towerdefense.orchestrator.systems.core.SystemPriority;

/**
 * Sélectionne les cibles pour chaque tour.
 * 
 * Responsabilités :
 * - Trouve l'ennemi le plus proche à portée pour chaque tour
 * - Maintient un cache des cibles pour le tick courant
 * - Applique la stratégie de ciblage (future extension)
 */
@Component
public class TargetingSystem implements GameSystem {
    
    /**
     * Cache des cibles sélectionnées pour le tick courant.
     * Clé : ID de la tour, Valeur : ID de l'ennemi ciblé
     */
    private final Map<EntityId, Enemy> targetCache = new HashMap<>();
    
    @Override
    public void process(GameState state, int tick, List<GameStateObserver> observers) {
        // Réinitialise le cache
        targetCache.clear();
        
        // Pour chaque tour prête à tirer
        for (Tower tower : state.towers()) {
            if (!tower.isReady()) {
                continue;
            }
            
            // Sélectionne la cible la plus proche
            findClosestTargetInRange(tower, state)
                .ifPresent(target -> targetCache.put(tower.id(), target));
        }
    }
    
    /**
     * Trouve la cible sélectionnée pour une tour donnée.
     * 
     * @param towerId l'ID de la tour
     * @return l'ID de l'ennemi ciblé, ou empty si aucune cible
     */
    public Optional<Enemy> getTarget(EntityId towerId) {
        return Optional.ofNullable(targetCache.get(towerId));
    }
    
    /**
     * Stratégie de ciblage : ennemi le plus proche.
     */
    private Optional<Enemy> findClosestTargetInRange(Tower tower, GameState state) {
        return state.enemies().stream()
            .filter(enemy -> tower.canShootTarget(enemy.position()))
            .min((e1, e2) -> Double.compare(
                tower.position().distanceTo(e1.position()),
                tower.position().distanceTo(e2.position())
            ));
    }
    
    @Override
    public SystemPriority priority() {
        return SystemPriority.HIGH;
    }
    
    /**
     * Nettoie le cache (appelé à la fin du tick).
     */
    public void clearCache() {
        targetCache.clear();
    }
}