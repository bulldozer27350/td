package com.towerdefense.domain.statik.level;

import java.util.List;

/**
 * Represents the definition of an attack in a tower defense game level.
 */
public class AttackDefinition {

    private final List<WaveDefinition> waves;

    /**
	 * Constructs an AttackDefinition with the specified list of wave definitions.
	 *
	 * @param waves the list of wave definitions for this attack
	 */
    public AttackDefinition(List<WaveDefinition> waves) {
        this.waves = List.copyOf(waves);
    }

    /**
     * Gets the list of wave definitions for this attack.
     * @return the list of wave definitions
     */
    public List<WaveDefinition> getWaves() {
        return waves;
    }
}

