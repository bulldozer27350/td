package com.towerdefense.config.registry;

import java.util.Map;

import com.towerdefense.domain.statik.enemy.EnemyFactory;
import com.towerdefense.domain.statik.enemy.EnemyFactoryProvider;

/**
 * Registry for enemy factories, allowing retrieval based on enemy type IDs.
 */
public class EnemyFactoryRegistry implements EnemyFactoryProvider {

	private final Map<String, EnemyFactory> factories;

	/** Constructor to initialize the registry with a map of enemy factories. */
	public EnemyFactoryRegistry(Map<String, EnemyFactory> factories) {
		this.factories = Map.copyOf(factories);
	}

	@Override
	/** Retrieves the enemy factory corresponding to the provided enemy type ID. */
	public EnemyFactory get(String enemyTypeId) {
		EnemyFactory factory = factories.get(enemyTypeId);
		if (factory == null) {
			throw new IllegalArgumentException("Unknown enemy type: " + enemyTypeId);
		}
		return factory;
	}

	@Override
	/** Checks if an enemy factory exists for the provided enemy type ID. */
	public boolean contains(String enemyTypeId) {
		return factories.containsKey(enemyTypeId);
	}
}
