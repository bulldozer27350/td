package com.towerdefense.editor.api.model.exportable;

/**
 * Defines which tower type is available in a level
 * and under which constraints.
 */
public class TowerCapacity {

    private final String towerType;
    private final int maxInstances;
    private final int maxUpgradeLevel;

    public TowerCapacity(
        String towerType,
        int maxInstances,
        int maxUpgradeLevel
    ) {
        if (towerType == null || towerType.isBlank()) {
            throw new IllegalArgumentException("towerType must not be null or blank");
        }
        if (maxInstances < 1) {
            throw new IllegalArgumentException("maxInstances must be >= 1");
        }
        if (maxUpgradeLevel < 0) {
            throw new IllegalArgumentException("maxUpgradeLevel must be >= 0");
        }

        this.towerType = towerType;
        this.maxInstances = maxInstances;
        this.maxUpgradeLevel = maxUpgradeLevel;
    }

    public String towerType() {
        return towerType;
    }

    public int maxInstances() {
        return maxInstances;
    }

    public int maxUpgradeLevel() {
        return maxUpgradeLevel;
    }
}
