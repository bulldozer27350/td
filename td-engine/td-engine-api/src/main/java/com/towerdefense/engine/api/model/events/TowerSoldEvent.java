package com.towerdefense.engine.api.model.events;

import java.util.UUID;

public record TowerSoldEvent(UUID towerId, int sellPrice, int tick) {

}
