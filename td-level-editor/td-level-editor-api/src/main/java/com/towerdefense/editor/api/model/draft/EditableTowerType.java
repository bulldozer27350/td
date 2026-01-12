package com.towerdefense.editor.api.model.draft;

import java.util.ArrayList;
import java.util.List;

public class EditableTowerType {

	private String id;
    private String towerType;
    private List<EditableTowerLevel> upgrades = new ArrayList<>();

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

    //////////////////////////////
    /// For Jackson usage only ///
    //////////////////////////////
    
	public void setUpgrades(List<EditableTowerLevel> upgrades) {
		this.upgrades = upgrades;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTowerType() {
		return towerType;
	}

	public void setTowerType(String towerType) {
		this.towerType = towerType;
	}

	public List<EditableTowerLevel> getUpgrades() {
		return upgrades;
	}
	
	

    
}
