package com.towerdefense.domain.statik.enemy;

/**
 * Fournit des usines d'ennemis basées sur un identifiant de type d'ennemi.
 */
public interface EnemyFactoryProvider {

	/** Récupère l'usine d'ennemis correspondant à l'identifiant fourni. */
    EnemyFactory get(String enemyTypeId);

    /** Vérifie si une usine d'ennemis existe pour l'identifiant fourni. */
    boolean contains(String enemyTypeId);
}

