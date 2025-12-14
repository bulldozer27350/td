package com.towerdefense.domain.intentions;

import com.towerdefense.domain.*;

public record PlaceTowerIntention(EntityId requestId, Position position, String towerType) {}
