package com.towerdefense.orchestrator.runtime.factory;

import java.util.List;
import java.util.Map;

import com.towerdefense.domain.map.EnemyPath;
import com.towerdefense.domain.statik.enemy.EnemyFactoryProvider;
import com.towerdefense.domain.statik.level.AttackDefinition;
import com.towerdefense.domain.statik.level.LevelScenarioDefinition;
import com.towerdefense.domain.statik.level.WaveDefinition;
import com.towerdefense.orchestrator.runtime.Attack;
import com.towerdefense.orchestrator.runtime.EnemyWave;
import com.towerdefense.orchestrator.runtime.LevelScenario;

/**
 * Factory class responsible for creating LevelScenario instances from their
 * definitions.
 */
public class LevelScenarioFactory {

	/**
	 * Create a LevelScenario from its definition, using provided enemy factories
	 * and paths.
	 */
	public LevelScenario create(LevelScenarioDefinition definition, EnemyFactoryProvider enemyFactories,
			Map<String, EnemyPath> paths) {
		List<Attack> attacks = definition.getAttacks().stream().map(a -> createAttack(a, enemyFactories, paths))
				.toList();

		return new LevelScenario(definition.getId(), attacks, definition.getTowerCapacities());
	}

	/**
	 * Create an Attack from its definition, using provided enemy factories and
	 * paths.
	 */
	private Attack createAttack(AttackDefinition definition, EnemyFactoryProvider enemyFactories,
			Map<String, EnemyPath> paths) {
		List<EnemyWave> waves = definition.getWaves().stream()
				.map(w -> new EnemyWave(w.getStartTick(), w.getSpawnInterval(), w.getSpawnInterval(), w.getCount(),
						enemyFactories.get(w.getEnemyTypeId()), resolvePath(w, paths)))
				.toList();

		return new Attack(waves);
	}

	/**
	 * Resolve the EnemyPath for a given WaveDefinition using the provided paths
	 * map.
	 * 
	 * @param w     the wave definition
	 * @param paths the map of path IDs to EnemyPath instances
	 * @return the resolved EnemyPath
	 */
	private EnemyPath resolvePath(WaveDefinition w, Map<String, EnemyPath> paths) {
		EnemyPath path = paths.get(w.getPathId());
		if (path == null) {
			throw new IllegalArgumentException("Unknown path id: " + w.getPathId());
		}
		return path;
	}
}
