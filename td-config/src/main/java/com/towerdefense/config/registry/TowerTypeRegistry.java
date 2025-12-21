package com.towerdefense.config.registry;

import com.towerdefense.domain.statik.tower.TowerType;

import java.util.Map;

public class TowerTypeRegistry {

    private final Map<String, TowerType> types;

    public TowerTypeRegistry(Map<String, TowerType> types) {
        this.types = Map.copyOf(types);
    }

    public TowerType get(String id) {
        TowerType type = types.get(id);
        if (type == null) {
            throw new IllegalArgumentException("Unknown tower type: " + id);
        }
        return type;
    }
}


