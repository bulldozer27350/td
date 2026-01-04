package com.towerdefense.editor.api.model.draft;

public class EditableEnemyType {

	private String id;
	private int health;
	private double speed;
	private int reward;

	@SuppressWarnings("unused")
	// For serialization
	private EditableEnemyType() {
	}

	public EditableEnemyType(String id, int health, double speed, int reward) {
		this.id = id;
		this.health = health;
		this.speed = speed;
		this.reward = reward;
	}

	public String getId() {
		return id;
	}

	public int getHealth() {
		return health;
	}

	public double getSpeed() {
		return speed;
	}

	public int getReward() {
		return reward;
	}

	public void setSpeed(double speed) {
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
