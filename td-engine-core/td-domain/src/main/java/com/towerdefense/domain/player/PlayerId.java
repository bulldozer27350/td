package com.towerdefense.domain.player;

import java.util.UUID;

public record PlayerId(UUID value) {
    public static PlayerId random() {
        return new PlayerId(UUID.randomUUID());
    }
}
