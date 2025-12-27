package com.towerdefense.config.assembler;

import com.towerdefense.config.mapper.LevelScenarioDefinitionMapper;
import com.towerdefense.domain.statik.level.LevelScenarioDefinition;
import com.towerdefense.engine.api.model.configuration.LevelConfig;

/** Configuration class for level scenarios in the tower defense game. */
public class LevelAssembler {

	/** Converts a LevelConfig DTO to a LevelScenarioDefinition domain object. */
    public LevelScenarioDefinition levelScenario(LevelConfig config) {
        return LevelScenarioDefinitionMapper.toDomain(config);
    }
}

