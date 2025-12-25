package com.towerdefense.config.mapper;


import com.towerdefense.config.dto.EnemyTypeConfig;
import com.towerdefense.domain.statik.enemy.BasicEnemyFactory;
import com.towerdefense.domain.statik.enemy.EnemyFactory;

public class EnemyFactoryMapper {

    public static EnemyFactory toDomain(EnemyTypeConfig cfg) {
        return new BasicEnemyFactory(
            cfg.getHp(),
            cfg.getSpeed(),
            cfg.getBounty()
        );
    }
}
