package com.towerdefense.engine.api.model.configuration;

/**
 * Represents a point in the tower defense game configuration.
 */
public class PointConfig {
    private int x;
    private int y;

    /** Get the x-coordinate of the point. */
    public int getX() { return x; }
    /** Get the y-coordinate of the point. */
    public int getY() { return y; }
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
    
    
}
