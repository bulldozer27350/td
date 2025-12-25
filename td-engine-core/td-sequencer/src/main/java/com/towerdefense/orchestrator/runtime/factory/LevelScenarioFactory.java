package com.towerdefense.orchestrator.runtime.factory;

import java.util.List;
import java.util.Map;

import com.towerdefense.domain.dynamik.level.AttackDefinition;
import com.towerdefense.domain.dynamik.level.LevelScenarioDefinition;
import com.towerdefense.domain.dynamik.level.WaveDefinition;
import com.towerdefense.domain.map.EnemyPath;
import com.towerdefense.domain.statik.enemy.EnemyFactoryProvider;
import com.towerdefense.orchestrator.runtime.Attack;
import com.towerdefense.orchestrator.runtime.EnemyWave;
import com.towerdefense.orchestrator.runtime.LevelScenario;

public class LevelScenarioFactory {

    public LevelScenario create(
        LevelScenarioDefinition definition,
        EnemyFactoryProvider enemyFactories,
        Map<String, EnemyPath> paths
    ) {
        List<Attack> attacks =
            definition.getAttacks().stream()
                .map(a -> createAttack(a, enemyFactories, paths))
                .toList();

        return new LevelScenario(definition.getId(), attacks);
    }

    private Attack createAttack(
        AttackDefinition definition,
        EnemyFactoryProvider enemyFactories,
        Map<String, EnemyPath> paths
    ) {
        List<EnemyWave> waves =
            definition.getWaves().stream()
                .map(w -> new EnemyWave(
                    w.getStartTick(),
                    w.getSpawnInterval(),
                    w.getSpawnInterval(),
                    w.getCount(),
                    enemyFactories.get(w.getEnemyTypeId()),
                    resolvePath(w, paths)
                ))
                .toList();

        return new Attack(waves);
    }

    private EnemyPath resolvePath(
        WaveDefinition w,
        Map<String, EnemyPath> paths
    ) {
        EnemyPath path = paths.get(w.getPathId());
        if (path == null) {
            throw new IllegalArgumentException(
                "Unknown path id: " + w.getPathId()
            );
        }
        return path;
    }
}


