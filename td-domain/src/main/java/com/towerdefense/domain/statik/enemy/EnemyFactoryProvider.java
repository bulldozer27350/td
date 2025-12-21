package com.towerdefense.domain.statik.enemy;

public interface EnemyFactoryProvider {

    EnemyFactory get(String enemyTypeId);

    boolean contains(String enemyTypeId);
}

