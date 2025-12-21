package com.towerdefense.config.spring;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.towerdefense.config.dto.TowersConfig;
import com.towerdefense.config.loader.JsonConfigLoader;
import com.towerdefense.config.mapper.TowerTypeMapper;
import com.towerdefense.config.registry.TowerTypeRegistry;
import com.towerdefense.domain.statik.tower.TowerType;

@Configuration
public class TowerTypeConfiguration {

    @Bean
    public TowerTypeRegistry towerTypeRegistry() {
        JsonConfigLoader loader = new JsonConfigLoader();
        TowersConfig config =
            loader.load("config/towers.json", TowersConfig.class);

        Map<String, TowerType> types =
            config.getTowers().stream()
                .map(TowerTypeMapper::toDomain)
                .collect(Collectors.toMap(TowerType::name, t -> t));

        return new TowerTypeRegistry(types);
    }
}


