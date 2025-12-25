package com.towerdefense.domain.intentions;

import com.towerdefense.domain.EntityId;

public record UpgradeTowerIntention(
        EntityId playerId,
        EntityId towerId
) {}
