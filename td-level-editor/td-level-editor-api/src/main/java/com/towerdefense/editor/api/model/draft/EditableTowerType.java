package com.towerdefense.editor.api.model.draft;

import java.util.ArrayList;
import java.util.List;

public class EditableTowerType {

	private String id;
    private String towerType;
    private final List<EditableTowerLevel> upgrades = new ArrayList<>();

    public EditableTowerType() {
	}
    
    public EditableTowerType(String id, String towerType) {
        this.id = id;
    	this.towerType = towerType;
    }

    public boolean addUpgrade(EditableTowerLevel upgrade) {
        return upgrades.add(upgrade);
    }

    public String towerType() { return towerType; }
    public String id() { return id; }
    public List<EditableTowerLevel> upgrades() { return upgrades; }
}
