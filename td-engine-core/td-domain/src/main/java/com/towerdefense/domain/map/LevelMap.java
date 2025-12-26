package com.towerdefense.domain.map;

/**
 * C'est une carte d'un niveau donné. Au travers de l'{@link EnemyPath}, une
 * carte possède un point de départ et un point d'arrivée.
 */
public class LevelMap {

	private final EnemyPath enemyPath;

	/**
	 * Crée une carte de niveau avec un chemin d'ennemis
	 * 
	 * @param enemyPath le chemin des ennemis sur la carte
	 */
	public LevelMap(EnemyPath enemyPath) {
		this.enemyPath = enemyPath;
	}

	/**
	 * Récupère le chemin des ennemis sur la carte
	 * 
	 * @return le chemin des ennemis
	 */
	public EnemyPath enemyPath() {
		return enemyPath;
	}
}
