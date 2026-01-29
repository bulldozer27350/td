package com.towerdefense.editor.api.model.draft;

import java.util.ArrayList;
import java.util.List;

public class EditableTowerType {

	private String id;
    private String towerType;
    private List<EditableTowerRank> ranks = new ArrayList<>();

    public EditableTowerType() {
	}
    
    public EditableTowerType(String id, String towerType) {
        this.id = id;
    	this.towerType = towerType;
    }

    public boolean addRank(EditableTowerRank rank) {
        return ranks.add(rank);
    }

    public String towerType() { return towerType; }
    public String id() { return id; }
    public List<EditableTowerRank> ranks() { return ranks; }

    //////////////////////////////
    /// For Jackson usage only ///
    //////////////////////////////
    
	public void setRanks(List<EditableTowerRank> ranks) {
		this.ranks = ranks;
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

	public List<EditableTowerRank> getRanks() {
		return ranks;
	}
	
	

    
}
