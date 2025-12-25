package com.towerdefense.config;

public class TowerUpgradeConfig {

    private int level;
    private int cost;
    private Integer damage; // nullable = pas de changement
	
    public TowerUpgradeConfig(int level, int cost, Integer damage) {
		super();
		this.level = level;
		this.cost = cost;
		this.damage = damage;
	}
    
	public int getLevel() {
		return level;
	}
	public int getCost() {
		return cost;
	}
	public Integer getDamage() {
		return damage;
	}

}
