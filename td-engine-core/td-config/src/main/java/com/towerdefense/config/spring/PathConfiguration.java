package com.towerdefense.config.spring;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.towerdefense.config.dto.PathsConfig;
import com.towerdefense.config.loader.JsonConfigLoader;
import com.towerdefense.config.mapper.EnemyPathMapper;
import com.towerdefense.domain.map.EnemyPath;

@Configuration
/**
 * Spring configuration class for loading enemy paths from JSON configuration.
 */
public class PathConfiguration {

    @Bean
    /** 
	 * Loads enemy paths from the JSON configuration file and maps them to domain objects.
	 * 
	 * @return a list of EnemyPath domain objects
	 */
    public List<EnemyPath> enemyPaths() {
        JsonConfigLoader loader = new JsonConfigLoader();
        PathsConfig config =
            loader.load("config/paths.json", PathsConfig.class);

        return config.getPaths().stream()
            .map(EnemyPathMapper::toDomain)
            .toList();
    }
}


