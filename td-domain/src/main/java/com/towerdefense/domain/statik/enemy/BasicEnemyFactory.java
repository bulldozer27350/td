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
	private final double speed;
	private int bounty;

	public BasicEnemyFactory(int hp, double speed, int bounty) {
		this.hp = hp;
		this.speed = speed;
		this.bounty = bounty;
	}

	@Override
	public Enemy create(EnemyPath path) {
		return new Enemy(EntityId.random(), path, hp, speed, bounty);
	}

}
