package com.towerdefense.services.internal.impl;

import com.towerdefense.domain.EntityId;
import com.towerdefense.domain.Position;
import com.towerdefense.domain.dynamik.tower.Tower;
import com.towerdefense.domain.statik.tower.TowerType;

public class TowerFactory {

    public Tower create(Position position, TowerType type) {
        return new Tower(
            EntityId.random(),
            position,
            type
        );
    }
}