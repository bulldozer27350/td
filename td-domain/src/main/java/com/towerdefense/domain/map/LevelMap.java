package com.towerdefense.domain.map;

/**
 * C'est une carte d'un niveau donné. Au travers de l'{@link EnemyPath}, une
 * carte possède un point de départ et un point d'arrivée.
 */
public class LevelMap {

	private final EnemyPath enemyPath;

	public LevelMap(EnemyPath enemyPath) {
		this.enemyPath = enemyPath;
	}

	public EnemyPath enemyPath() {
		return enemyPath;
	}
}
