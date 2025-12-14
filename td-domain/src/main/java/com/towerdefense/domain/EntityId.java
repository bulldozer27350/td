package com.towerdefense.domain;

import java.util.UUID;

public record EntityId(UUID value) {
	public static EntityId random() {
		return new EntityId(UUID.randomUUID());
	}
}