package com.towerdefense.services.internal.impl;

import com.towerdefense.domain.EntityId;
import com.towerdefense.domain.Position;
import com.towerdefense.domain.dynamik.tower.Tower;
import com.towerdefense.domain.statik.tower.TowerType;

/**
 * Factory class for creating Tower instances.
 */
public class TowerFactory {

	/** Create a new Tower instance at the specified position with the given tower type. */
    public Tower create(Position position, TowerType type) {
        return new Tower(
            EntityId.random(),
            position,
            type
        );
    }
}