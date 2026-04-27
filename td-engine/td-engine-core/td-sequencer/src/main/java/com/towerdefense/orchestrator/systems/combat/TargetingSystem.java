package com.towerdefense.orchestrator.systems.combat;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.towerdefense.domain.EntityId;
import com.towerdefense.domain.GameState;
import com.towerdefense.domain.dynamik.enemy.Enemy;
import com.towerdefense.domain.dynamik.tower.Tower;

/**
 * Sélectionne les cibles pour chaque tour.
 * 
 * Responsabilités : - Trouve l'ennemi le plus proche à portée pour chaque tour
 * - Applique la stratégie de ciblage (future extension)
 * Ce composant est sans état (stateless).
 */
@Component
public class TargetingSystem {

    /**
     * Stratégie de ciblage actuelle. Par défaut : CLOSEST_TO_EXIT
     */
    private TargetingStrategy strategy = TargetingStrategy.CLOSEST_TO_EXIT;

    /**
     * Sélectionne une cible selon la stratégie configurée.
     */
    public Optional<Enemy> findTarget(Tower tower, GameState state) {
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
}