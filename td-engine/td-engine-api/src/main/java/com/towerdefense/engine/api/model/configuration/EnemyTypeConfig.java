package com.towerdefense.engine.api.model.configuration;

/**
 * Represents the configuration for a specific type of enemy in the tower defense game.
 */
public class EnemyTypeConfig {

    private String id;
    private int hp;
    private double speed;
    private int bounty;

    /** Get the unique identifier of the enemy type. */
    public String getId() { return id; }
    /** Get the health points of the enemy type. */
    public int getHp() { return hp; }
    /** Get the speed of the enemy type. */
    public double getSpeed() { return speed; }
    /** Get the bounty awarded for defeating the enemy type. */
    public int getBounty() { return bounty; }
}

