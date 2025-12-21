package com.towerdefense.config.spring;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.towerdefense.config.dto.PathsConfig;
import com.towerdefense.config.loader.JsonConfigLoader;
import com.towerdefense.config.mapper.EnemyPathMapper;
import com.towerdefense.domain.map.EnemyPath;

@Configuration
public class PathConfiguration {

    @Bean
    public List<EnemyPath> enemyPaths() {
        JsonConfigLoader loader = new JsonConfigLoader();
        PathsConfig config =
            loader.load("config/paths.json", PathsConfig.class);

        return config.getPaths().stream()
            .map(EnemyPathMapper::toDomain)
            .toList();
    }
}


