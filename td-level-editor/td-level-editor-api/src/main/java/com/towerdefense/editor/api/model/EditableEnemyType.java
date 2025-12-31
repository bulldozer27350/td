package com.towerdefense.editor.api.model;

public class EditableEnemyType {

	private String id;
	private int health;
	private int speed;
	private int reward;

	@SuppressWarnings("unused")
	// For serialization
	private EditableEnemyType() {
	}

	public EditableEnemyType(String id, int health, int speed, int reward) {
		this.id = id;
		this.health = health;
		this.speed = speed;
		this.reward = reward;
	}

	public String getInd() {
		return id;
	}

	public int getHealth() {
		return health;
	}

	public int getSpeed() {
		return speed;
	}

	public int getReward() {
		return reward;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setReward(int reward) {
		this.reward = reward;
	}

}
