package com.towerdefense.config.mapper;


import com.towerdefense.config.dto.EnemyTypeConfig;
import com.towerdefense.domain.statik.enemy.BasicEnemyFactory;
import com.towerdefense.domain.statik.enemy.EnemyFactory;

/**
 * Mapper class to convert EnemyTypeConfig DTOs to EnemyFactory domain objects.
 */
public class EnemyFactoryMapper {

	/** Converts an EnemyTypeConfig DTO to an EnemyFactory domain object. */
    public static EnemyFactory toDomain(EnemyTypeConfig cfg) {
        return new BasicEnemyFactory(
            cfg.getHp(),
            cfg.getSpeed(),
            cfg.getBounty()
        );
    }
}
