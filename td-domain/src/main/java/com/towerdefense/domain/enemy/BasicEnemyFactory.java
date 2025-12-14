package com.towerdefense.domain.enemy;

import com.towerdefense.domain.EntityId;
import com.towerdefense.domain.map.EnemyPath;

/**
 * Usine créant des ennemis basiques : chaque ennemi créé aura donc son nombre
 * de HP et sa vitesse. C'est lors de la création d'un ennemi que le chemin à
 * suivre lui sera fourni.
 */
public class BasicEnemyFactory implements EnemyFactory {

	private final int hp;
	private final double speed;

	public BasicEnemyFactory(int hp, double speed) {
		this.hp = hp;
		this.speed = speed;
	}

	@Override
	public Enemy create(EnemyPath path) {
		return new Enemy(EntityId.random(), path, hp, speed);
	}

}
