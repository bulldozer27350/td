package com.towerdefense.domain.statik.tower;

import java.util.List;

public class TowerType {

    private final String name;
    private final List<TowerLevelDefinition> levels;

    public TowerType(String name, List<TowerLevelDefinition> levels) {
        this.name = name;
        this.levels = List.copyOf(levels);
    }

    public TowerLevelDefinition level(int level) {
        return levels.get(level - 1);
    }

    public int maxLevel() {
        return levels.size();
    }
    
    public String name() {
    	return name;
    }
}

