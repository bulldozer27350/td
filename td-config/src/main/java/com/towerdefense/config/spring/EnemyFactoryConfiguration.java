package com.towerdefense.config.spring;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.towerdefense.config.dto.EnemiesConfig;
import com.towerdefense.config.dto.EnemyTypeConfig;
import com.towerdefense.config.loader.JsonConfigLoader;
import com.towerdefense.config.registry.EnemyFactoryRegistry;
import com.towerdefense.domain.statik.enemy.BasicEnemyFactory;
import com.towerdefense.domain.statik.enemy.EnemyFactory;

@Configuration
public class EnemyFactoryConfiguration {

    @Bean
    public EnemyFactoryRegistry enemyFactoryRegistry() {
    	JsonConfigLoader loader = new JsonConfigLoader();
        EnemiesConfig config =
            loader.load("config/enemies.json", EnemiesConfig.class);

        Map<String, EnemyFactory> factories =
            config.getEnemies().stream()
                .collect(Collectors.toMap(
                    EnemyTypeConfig::getId,
                    e -> new BasicEnemyFactory(
                        e.getHp(),
                        e.getSpeed(),
                        e.getBounty()
                    )
                ));

        return new EnemyFactoryRegistry(factories);
    }
}
