package com.towerdefense.config.dto;

import java.util.List;

public class LevelConfig {

	private int id;
	private List<AttackConfig> attacks;

	public int getId() {
		return id;
	}

	public List<AttackConfig> getAttacks() {
		return attacks;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setAttacks(List<AttackConfig> attacks) {
		this.attacks = attacks;
	}
	
	
}
