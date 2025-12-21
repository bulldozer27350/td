package com.towerdefense.config.mapper;

import com.towerdefense.config.dto.AttackConfig;
import com.towerdefense.config.dto.LevelConfig;
import com.towerdefense.config.dto.WaveConfig;
import com.towerdefense.domain.dynamik.level.AttackDefinition;
import com.towerdefense.domain.dynamik.level.LevelScenarioDefinition;
import com.towerdefense.domain.dynamik.level.WaveDefinition;

public class LevelScenarioDefinitionMapper {

    public static LevelScenarioDefinition toDomain(LevelConfig cfg) {
        LevelScenarioDefinition levelScenarioDefinition = new LevelScenarioDefinition(
            cfg.getId(),
            cfg.getAttacks().stream()
                .map(LevelScenarioDefinitionMapper::toAttack)
                .toList()
        );
        System.out.println("DEBUG LEVEL SCENARIO DEFINITION ATTACKS :" +levelScenarioDefinition.getAttacks());
		return levelScenarioDefinition;
    }

    private static AttackDefinition toAttack(AttackConfig cfg) {
        return new AttackDefinition(
            cfg.getWaves().stream()
                .map(LevelScenarioDefinitionMapper::toWave)
                .toList()
        );
    }

    private static WaveDefinition toWave(WaveConfig cfg) {
        return new WaveDefinition(
            cfg.getStartTick(),
            cfg.getSpawnInterval(),
            cfg.getCount(),
            cfg.getEnemyType(),
            cfg.getPathId()
        );
    }
}
