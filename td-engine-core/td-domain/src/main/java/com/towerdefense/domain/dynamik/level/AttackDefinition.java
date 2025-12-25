package com.towerdefense.domain.dynamik.level;

import java.util.List;

public class AttackDefinition {

    private final List<WaveDefinition> waves;

    public AttackDefinition(List<WaveDefinition> waves) {
        this.waves = List.copyOf(waves);
    }

    public List<WaveDefinition> getWaves() {
        return waves;
    }
}

