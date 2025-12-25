package com.towerdefense.domain.intentions;

import com.towerdefense.domain.EntityId;

public record SellTowerIntention(
        EntityId playerId,
        EntityId towerId
) {}
