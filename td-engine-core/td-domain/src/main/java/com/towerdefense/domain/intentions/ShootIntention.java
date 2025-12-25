package com.towerdefense.domain.intentions;

import com.towerdefense.domain.*;

public record ShootIntention(EntityId towerId, EntityId targetId) {}
