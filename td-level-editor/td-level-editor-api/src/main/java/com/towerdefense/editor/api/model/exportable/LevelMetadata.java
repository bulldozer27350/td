package com.towerdefense.editor.api.model.exportable;

public class LevelMetadata {

	private String id;
	private int startingMoney;
	private int startingLives;

	@SuppressWarnings("unused")
	// For serialization
	private LevelMetadata() {
	}
	
	public LevelMetadata(String id, int startingMoney,
			int startingLives) {
		this.id = id;
		this.startingMoney = startingMoney;
		this.startingLives = startingLives;
	}

	public String getId() {
		return id;
	}

	public void setId(String name) {
		this.id = name;
	}

	public int getStartingMoney() {
		return startingMoney;
	}

	public void setStartingMoney(int startingMoney) {
		this.startingMoney = startingMoney;
	}

	public int getStartingLives() {
		return startingLives;
	}

	public void setStartingLives(int startingLives) {
		this.startingLives = startingLives;
	}

}
