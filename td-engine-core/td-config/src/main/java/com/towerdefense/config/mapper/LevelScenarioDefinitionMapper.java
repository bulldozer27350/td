package com.towerdefense.config.mapper;

import com.towerdefense.config.dto.AttackConfig;
import com.towerdefense.config.dto.LevelConfig;
import com.towerdefense.config.dto.WaveConfig;
import com.towerdefense.domain.dynamik.level.AttackDefinition;
import com.towerdefense.domain.dynamik.level.LevelScenarioDefinition;
import com.towerdefense.domain.dynamik.level.WaveDefinition;

/**
 * Mapper class to convert LevelConfig DTOs to LevelScenarioDefinition domain objects.
 */
public class LevelScenarioDefinitionMapper {

	/**
	 * Converts a LevelConfig DTO to a LevelScenarioDefinition domain object.
	 *
	 * @param cfg the LevelConfig DTO
	 * @return the corresponding LevelScenarioDefinition domain object
	 */
    public static LevelScenarioDefinition toDomain(LevelConfig cfg) {
        LevelScenarioDefinition levelScenarioDefinition = new LevelScenarioDefinition(
            cfg.getId(),
            cfg.getAttacks().stream()
                .map(LevelScenarioDefinitionMapper::toAttack)
                .toList()
        );
		return levelScenarioDefinition;
    }

    /** Converts an AttackConfig DTO to an AttackDefinition domain object. */
    private static AttackDefinition toAttack(AttackConfig cfg) {
        return new AttackDefinition(
            cfg.getWaves().stream()
                .map(LevelScenarioDefinitionMapper::toWave)
                .toList()
        );
    }

    /** Converts a WaveConfig DTO to a WaveDefinition domain object. */
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
