package com.towerdefense.orchestrator.systems.combat;

/**
 * Énumération des stratégies de ciblage disponibles pour les tours.
 */
public enum TargetingStrategy {
    /**
     * ✅ RECOMMANDÉ : Cible l'ennemi le plus PROCHE DE LA SORTIE.
     * Calcule la distance restante jusqu'à la fin du chemin.
     * Fonctionne correctement avec des chemins de longueurs variables.
     */
    CLOSEST_TO_EXIT,

    /**
     * Cible l'ennemi qui a PARCOURU le plus de distance sur son chemin.
     * ⚠️ Fonctionne bien uniquement si tous les chemins ont la même longueur.
     */
    MOST_ADVANCED,

    /**
     * Cible l'ennemi le plus proche PHYSIQUEMENT de la tour.
     * Stratégie originale.
     */
    CLOSEST_TO_TOWER,

    /**
     * Cible l'ennemi avec le PLUS de HP (focus tanks).
     */
    STRONGEST,

    /**
     * Cible l'ennemi avec le MOINS de HP (finir les blessés).
     */
    WEAKEST,

    /**
     * Cible le PREMIER ennemi apparu (FIFO).
     */
    FIRST
}
