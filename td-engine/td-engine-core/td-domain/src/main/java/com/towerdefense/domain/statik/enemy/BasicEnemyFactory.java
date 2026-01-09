package com.towerdefense.domain.statik.enemy;

import com.towerdefense.domain.EntityId;
import com.towerdefense.domain.dynamik.enemy.Enemy;
import com.towerdefense.domain.map.EnemyPath;

/**
 * Usine créant des ennemis basiques : chaque ennemi créé aura donc son nombre
 * de HP et sa vitesse. C'est lors de la création d'un ennemi que le chemin à
 * suivre lui sera fourni.
 */
public class BasicEnemyFactory implements EnemyFactory {

	private final int hp;
	/** Vitesse de déplacement en cases par seconde */
	private final double speedCasesPerSecond;
	private int bounty;

	/** Constructeur de l'usine d'ennemis basiques. */
	public BasicEnemyFactory(int hp, double speedCasesPerSecond, int bounty) {
		this.hp = hp;
		this.speedCasesPerSecond = speedCasesPerSecond;
		this.bounty = bounty;
	}

	@Override
	/** Crée un ennemi basique avec le chemin fourni. */
	public Enemy create(EnemyPath path) {
		// La conversion en cases/tick est gérée dans le constructeur d'Enemy
		return new Enemy(EntityId.random(), path, hp, speedCasesPerSecond, bounty);
	}

}
