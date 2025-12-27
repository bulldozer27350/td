package com.towerdefense.domain.intentions;

import com.towerdefense.domain.EntityId;

/**
 * Represents the intention of a tower to shoot at a target.
 *
 * @param towerId  the ID of the tower intending to shoot
 * @param targetId the ID of the target to be shot at
 */
public record ShootIntention(EntityId towerId, EntityId targetId) {}
