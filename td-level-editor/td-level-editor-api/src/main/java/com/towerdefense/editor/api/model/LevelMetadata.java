package com.towerdefense.editor.api.model;

public class LevelMetadata {

	private int levelId;
	private String name;
	private String description;
	private int startingMoney;
	private int startingLives;

	@SuppressWarnings("unused")
	// For serialization
	private LevelMetadata() {
	}
	
	public LevelMetadata(int levelId, String name, String description, int startingMoney,
			int startingLives) {
		super();
		this.levelId = levelId;
		this.name = name;
		this.description = description;
		this.startingMoney = startingMoney;
		this.startingLives = startingLives;
	}

	public int getLevelId() {
		return levelId;
	}

	public String getName() {
		return name;
	}

	public LevelMetadata setName(String name) {
		this.name = name;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public LevelMetadata setDescription(String description) {
		this.description = description;
		return this;
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
