package com.towerdefense.config.dto;

import java.util.List;

public class TowerTypeConfig {

    private String id;
    private List<TowerLevelConfig> levels;

    public String getId() { return id; }
    public List<TowerLevelConfig> getLevels() { return levels; }
}

