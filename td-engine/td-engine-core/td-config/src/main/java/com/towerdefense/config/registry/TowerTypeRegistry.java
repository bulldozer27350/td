package com.towerdefense.config.registry;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.towerdefense.domain.statik.tower.TowerType;

/**
 * Registry for tower types in the tower defense game.
 */
@Component
public class TowerTypeRegistry {

    private final Map<String, TowerType> types;

    /** Constructor to initialize the registry with a map of tower types. */
    public TowerTypeRegistry(Map<String, TowerType> types) {
        this.types = Map.copyOf(types);
    }

    /** Get a tower type by its unique identifier. */
    public TowerType get(String id) {
        TowerType type = types.get(id);
        if (type == null) {
            throw new IllegalArgumentException("Unknown tower type: " + id);
        }
        return type;
    }
}


