package com.towerdefense.editor.api.model.draft;

import java.util.ArrayList;
import java.util.List;

public class EditableLevel {

	private String id;
	private int startingMoney;
	private int startingLives;
    private EditableMap map;
    private List<EditablePath> paths = new ArrayList<>();
    private List<EditableAttack> attacks = new ArrayList<>();
    
    private List<TowerCapacity> towerCapacities = new ArrayList<>();

    @SuppressWarnings("unused")
    private EditableLevel() {}
    
    public EditableLevel(
    		String id,
    		int startingMoney,
    		int startingLives,
            EditableMap editableMap, 
            List<EditableAttack> attacks,
            List<TowerCapacity> towerCapacities) {
    	this.id = id;
    	this.startingMoney = startingMoney;
    	this.startingLives = startingLives;
        this.map = editableMap;
        this.attacks = attacks;
        this.paths = editableMap.paths();
        this.towerCapacities = towerCapacities;
    }

    public String getId() {
		return id;
	}

	public int getStartingMoney() {
		return startingMoney;
	}

	public int getStartingLives() {
		return startingLives;
	}

	public EditableMap getMap() { return map; }
    public List<EditablePath> getPaths() { return paths; }
    public List<EditableAttack> getAttacks() { return attacks; }
    public List<TowerCapacity> getTowerCapacities() { return towerCapacities; }

	public void setId(String id) {
		this.id = id;
	}

	public void setStartingMoney(int startingMoney) {
		this.startingMoney = startingMoney;
	}

	public void setStartingLives(int startingLives) {
		this.startingLives = startingLives;
	}

	public void setMap(EditableMap map) {
		this.map = map;
	}

	public void setPaths(List<EditablePath> paths) {
		this.paths = paths;
	}

	public void setAttacks(List<EditableAttack> attacks) {
		this.attacks = attacks;
	}

	public void setTowerCapacities(List<TowerCapacity> towerCapacities) {
		this.towerCapacities = towerCapacities;
	}

}