package com.towerdefense.config.registry;

import java.util.Map;

import com.towerdefense.domain.statik.enemy.EnemyFactory;
import com.towerdefense.domain.statik.enemy.EnemyFactoryProvider;

public class EnemyFactoryRegistry implements EnemyFactoryProvider {

	private final Map<String, EnemyFactory> factories;

	public EnemyFactoryRegistry(Map<String, EnemyFactory> factories) {
		this.factories = Map.copyOf(factories);
	}

	@Override
	public EnemyFactory get(String enemyTypeId) {
		EnemyFactory factory = factories.get(enemyTypeId);
		if (factory == null) {
			throw new IllegalArgumentException("Unknown enemy type: " + enemyTypeId);
		}
		return factory;
	}

	@Override
	public boolean contains(String enemyTypeId) {
		return factories.containsKey(enemyTypeId);
	}
}
