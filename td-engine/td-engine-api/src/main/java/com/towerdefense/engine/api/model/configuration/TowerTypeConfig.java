package com.towerdefense.engine.api.model.configuration;

import java.util.List;

/**
 * Represents the configuration for a tower type in the tower defense game.
 */
public class TowerTypeConfig {

    private String id;
    private List<TowerLevelConfig> levels;

    /** Get the unique identifier of the tower type. */
    public String getId() { return id; }
    /** Get the list of tower level configurations. */
    public List<TowerLevelConfig> getLevels() { return levels; }
}

