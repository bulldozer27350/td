package com.towerdefense.domain;

/**
 * Represents a position in a 2D space with x and y coordinates.
 */
public record Position(double x, double y) implements GameObject {

	/**
	 * Calculates the Euclidean distance between this position and another position.
	 *
	 * @param other The other position to calculate the distance to.
	 * @return The Euclidean distance between the two positions.
	 */
	public double distanceTo(Position other) {
		double dx = other.x - x;
		double dy = other.y - y;
		return Math.sqrt(dx * dx + dy * dy);
	}

    @Override
    public Position position() {
        return this;
    }
}