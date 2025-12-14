package com.towerdefense.domain.enemy;

import com.towerdefense.domain.map.EnemyPath;

/**
 * Gère une usine d'ennemis. Chaque usine sait quel type d'ennemi créer et
 * quelle est la caractéristique des ennemis à créer. Sur chaque usine, on
 * applique un chemin pour que chaque ennemi provenant d'une même usine suive le
 * même chemin.
 */
public interface EnemyFactory {
	Enemy create(EnemyPath path);
}