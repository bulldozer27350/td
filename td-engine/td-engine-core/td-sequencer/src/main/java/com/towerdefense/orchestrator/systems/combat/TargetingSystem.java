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
 * Responsabilités : - Trouve l'ennemi le plus proche à portée pour chaque tour
 * - Maintient un cache des cibles pour le tick courant - Applique la stratégie
 * de ciblage (future extension)
 */
@Component
public class TargetingSystem implements GameSystem {

    /**
     * Cache des cibles sélectionnées pour le tick courant. Clé : ID de la tour,
     * Valeur : ID de l'ennemi ciblé
     */
    private final Map<EntityId, Enemy> targetCache = new HashMap<>();

    /**
     * Stratégie de ciblage actuelle. Par défaut : CLOSEST_TO_EXIT
     */
    private TargetingStrategy strategy = TargetingStrategy.CLOSEST_TO_EXIT;

    @Override
    public void process(GameState state, int tick, List<GameStateObserver> observers) {
        // Réinitialise le cache
        targetCache.clear();

        // Pour chaque tour prête à tirer
        for (Tower tower : state.towers()) {
            if (!tower.isReady()) {
                continue;
            }

            // Sélectionne la cible selon la stratégie
            findTarget(tower, state).ifPresent(target -> targetCache.put(tower.id(), target));
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
     * Sélectionne une cible selon la stratégie configurée.
     */
    private Optional<Enemy> findTarget(Tower tower, GameState state) {
        return switch (strategy) {
        case CLOSEST_TO_EXIT -> findClosestToExit(tower, state);
        case MOST_ADVANCED -> findMostAdvanced(tower, state);
        case CLOSEST_TO_TOWER -> findClosestToTower(tower, state);
        case STRONGEST -> findStrongest(tower, state);
        case WEAKEST -> findWeakest(tower, state);
        case FIRST -> findFirst(tower, state);
        };
    }

    // ========================================================================
    // STRATÉGIES DE CIBLAGE
    // ========================================================================

    /**
     * ✅ STRATÉGIE RECOMMANDÉE : L'ennemi le plus PROCHE DE LA SORTIE. Calcule la
     * distance RESTANTE et cible celui qui arrivera en PREMIER.
     */
    private Optional<Enemy> findClosestToExit(Tower tower, GameState state) {
        return state.enemies().stream().filter(enemy -> tower.canShootTarget(enemy.position()))
                .min((e1, e2) -> Double.compare(calculateRemainingDistance(e1), calculateRemainingDistance(e2)));
    }

    /**
     * Calcule la distance restante pour un ennemi.
     */
    private double calculateRemainingDistance(Enemy enemy) {
        double totalLength = enemy.path().totalLength();
        double remaining = totalLength - enemy.distanceTraveled();
        return Math.max(0.0, remaining);
    }

    /**
     * L'ennemi qui a PARCOURU le plus de distance. ⚠️ Fonctionne bien uniquement si
     * tous les chemins ont la même longueur.
     */
    private Optional<Enemy> findMostAdvanced(Tower tower, GameState state) {
        return state.enemies().stream().filter(enemy -> tower.canShootTarget(enemy.position()))
                .max((e1, e2) -> Double.compare(e1.distanceTraveled(), e2.distanceTraveled()));
    }

    /**
     * L'ennemi le plus proche PHYSIQUEMENT de la tour. Stratégie originale.
     */
    private Optional<Enemy> findClosestToTower(Tower tower, GameState state) {
        return state.enemies().stream().filter(enemy -> tower.canShootTarget(enemy.position())).min((e1, e2) -> Double
                .compare(tower.position().distanceTo(e1.position()), tower.position().distanceTo(e2.position())));
    }

    /**
     * L'ennemi avec le PLUS de HP.
     */
    private Optional<Enemy> findStrongest(Tower tower, GameState state) {
        return state.enemies().stream().filter(enemy -> tower.canShootTarget(enemy.position()))
                .max((e1, e2) -> Integer.compare(e1.health().current(), e2.health().current()));
    }

    /**
     * L'ennemi avec le MOINS de HP (finir les blessés).
     */
    private Optional<Enemy> findWeakest(Tower tower, GameState state) {
        return state.enemies().stream().filter(enemy -> tower.canShootTarget(enemy.position()))
                .min((e1, e2) -> Integer.compare(e1.health().current(), e2.health().current()));
    }

    /**
     * Le PREMIER ennemi entré dans le jeu (FIFO).
     */
    private Optional<Enemy> findFirst(Tower tower, GameState state) {
        return state.enemies().stream().filter(enemy -> tower.canShootTarget(enemy.position())).findFirst();
    }

    /**
     * Change la stratégie de ciblage.
     * 
     * @param strategy la nouvelle stratégie
     */
    public void setStrategy(TargetingStrategy strategy) {
        this.strategy = strategy;
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