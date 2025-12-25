package com.towerdefense.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.towerdefense.config.dto.LevelConfig;
import com.towerdefense.config.loader.JsonConfigLoader;
import com.towerdefense.config.mapper.LevelScenarioDefinitionMapper;
import com.towerdefense.domain.dynamik.level.LevelScenarioDefinition;

@Configuration
public class LevelConfiguration {

    @Bean
    public LevelScenarioDefinition levelScenario() {
        JsonConfigLoader loader = new JsonConfigLoader();
        LevelConfig config =
            loader.load("config/levels/level-1.json", LevelConfig.class);

        return LevelScenarioDefinitionMapper.toDomain(config);
    }
}

