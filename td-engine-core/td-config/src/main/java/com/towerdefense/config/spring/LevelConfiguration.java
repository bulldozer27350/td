package com.towerdefense.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.towerdefense.config.dto.LevelConfig;
import com.towerdefense.config.loader.JsonConfigLoader;
import com.towerdefense.config.mapper.LevelScenarioDefinitionMapper;
import com.towerdefense.domain.dynamik.level.LevelScenarioDefinition;

@Configuration
/**
 * Spring configuration class for loading level scenarios in the tower defense game.
 */
public class LevelConfiguration {

    @Bean
    /** 
	 * Loads the level scenario definition from a JSON configuration file.
	 * 
	 * @return LevelScenarioDefinition representing the level scenario.
	 */
    public LevelScenarioDefinition levelScenario() {
        JsonConfigLoader loader = new JsonConfigLoader();
        LevelConfig config =
            loader.load("config/levels/level-1.json", LevelConfig.class);

        return LevelScenarioDefinitionMapper.toDomain(config);
    }
}

