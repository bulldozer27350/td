package com.towerdefense.config.mapper;

import java.util.List;

import com.towerdefense.domain.statik.level.AttackDefinition;
import com.towerdefense.domain.statik.level.LevelScenarioDefinition;
import com.towerdefense.domain.statik.level.TowerCapacityDefinition;
import com.towerdefense.domain.statik.level.WaveDefinition;
import com.towerdefense.engine.api.model.configuration.AttackConfig;
import com.towerdefense.engine.api.model.configuration.LevelConfig;
import com.towerdefense.engine.api.model.configuration.WaveConfig;

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
	    List<TowerCapacityDefinition> capacities = cfg.getTowerCapacities().stream()
	        .map(tc -> new TowerCapacityDefinition(tc.getTowerTypeId(), tc.getMaxRank()))
	        .toList();
	    
	    LevelScenarioDefinition levelScenarioDefinition = new LevelScenarioDefinition(
	        cfg.getId(),
	        cfg.getAttacks().stream()
	            .map(LevelScenarioDefinitionMapper::toAttack)
	            .toList(),
	        cfg.getStartingLives(),
	        cfg.getStartingMoney(),
	        capacities
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
