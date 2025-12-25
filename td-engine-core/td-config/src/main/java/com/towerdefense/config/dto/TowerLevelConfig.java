package com.towerdefense.config.dto;

public class TowerLevelConfig {

    private int level;
    private int upgradeCost;
    private int sellValue;
    private double range;
    private int damage;
    private double reloadSeconds;
    private int buildTimeTicks;

    public int getLevel() { return level; }
    public int getUpgradeCost() { return upgradeCost; }
    public int getSellValue() { return sellValue; }
    public double getRange() { return range; }
    public int getDamage() { return damage; }
    public double getReloadSeconds() { return reloadSeconds; }
    public int getBuildTimeTicks() { return buildTimeTicks; }
}

