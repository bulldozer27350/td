package com.towerdefense.engine.api.model.events;

import java.util.UUID;

public record TowerUpgradedEvent(UUID towerId, int level, int tick) {}
