package com.towerdefense.config.dto;

import java.util.List;

/**
 * Represents the configuration for enemy types in the tower defense game.
 */
public class EnemiesConfig {

    private List<EnemyTypeConfig> enemies;

    /** Get the list of enemy type configurations. */
    public List<EnemyTypeConfig> getEnemies() {
        return enemies;
    }
}
