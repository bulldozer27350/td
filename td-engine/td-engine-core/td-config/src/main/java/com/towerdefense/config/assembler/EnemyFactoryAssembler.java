package com.towerdefense.config.assembler;

import java.util.Map;
import java.util.stream.Collectors;

import com.towerdefense.config.registry.EnemyFactoryRegistry;
import com.towerdefense.domain.statik.enemy.BasicEnemyFactory;
import com.towerdefense.domain.statik.enemy.EnemyFactory;
import com.towerdefense.engine.api.model.configuration.EnemiesConfig;
import com.towerdefense.engine.api.model.configuration.EnemyTypeConfig;

/**
 * Configuration class for setting up enemy factories based on the provided configuration.
 */
public class EnemyFactoryAssembler {

	/** Creates an EnemyFactoryRegistry from the given EnemiesConfig. */
	public EnemyFactoryRegistry enemyFactoryRegistry(EnemiesConfig config) {
		Map<String, EnemyFactory> factories = config.getEnemies().stream().collect(Collectors
				.toMap(EnemyTypeConfig::getId, e -> new BasicEnemyFactory(e.getHp(), e.getSpeed(), e.getBounty())));

		return new EnemyFactoryRegistry(factories);
	}
}
