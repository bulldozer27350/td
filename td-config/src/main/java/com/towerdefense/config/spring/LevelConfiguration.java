package com.towerdefense.config.spring;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.towerdefense.config.dto.LevelConfig;
import com.towerdefense.config.loader.JsonConfigLoader;
import com.towerdefense.config.mapper.LevelScenarioDefinitionMapper;
import com.towerdefense.config.registry.EnemyFactoryRegistry;
import com.towerdefense.domain.dynamik.level.LevelScenarioDefinition;
import com.towerdefense.domain.map.EnemyPath;

@Configuration
public class LevelConfiguration {

    @Bean
    public LevelScenarioDefinition levelScenario(
        EnemyFactoryRegistry enemyFactories,
        List<EnemyPath> paths
    ) {
        JsonConfigLoader loader = new JsonConfigLoader();
        LevelConfig config =
            loader.load("config/levels/level-1.json", LevelConfig.class);

        System.out.println("DEBUG attacks = " + config.getAttacks());
        return LevelScenarioDefinitionMapper.toDomain(config);
    }
}

