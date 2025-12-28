package com.towerdefense.editor.api.model;

public class LevelMetadata {

	private final int levelId;
	private String name;
	private String description;
	private int recommendedDifficulty;
	private int startingMoney;
	private int startingLives;

	public LevelMetadata(int levelId, String name, String description, int recommendedDifficulty, int startingMoney,
			int startingLives) {
		super();
		this.levelId = levelId;
		this.name = name;
		this.description = description;
		this.recommendedDifficulty = recommendedDifficulty;
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

	public int getRecommendedDifficulty() {
		return recommendedDifficulty;
	}

	public void setRecommendedDifficulty(int recommendedDifficulty) {
		this.recommendedDifficulty = recommendedDifficulty;
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
