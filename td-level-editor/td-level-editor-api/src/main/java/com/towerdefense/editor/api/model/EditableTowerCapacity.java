package com.towerdefense.editor.api.model;

import java.util.ArrayList;
import java.util.List;

public class EditableTowerCapacity {

    private String towerType;
    private final List<TowerUpgrade> upgrades = new ArrayList<>();

    public EditableTowerCapacity() {
	}
    
    public EditableTowerCapacity(String towerType) {
        this.towerType = towerType;
    }

    public void addUpgrade(TowerUpgrade upgrade) {
        upgrades.add(upgrade);
    }

    public String towerType() { return towerType; }
    public List<TowerUpgrade> upgrades() { return upgrades; }
}
