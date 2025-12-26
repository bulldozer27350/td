package com.towerdefense.config.dto;

import java.util.List;

/**
 * Represents the configuration for attack waves in the tower defense game.
 */
public class AttackConfig {

    private List<WaveConfig> waves;

    /** Get the list of wave configurations. */
    public List<WaveConfig> getWaves() {
        return waves;
    }
}

