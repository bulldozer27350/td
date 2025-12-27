package com.towerdefense.engine.api.model.configuration;

import java.util.List;

/**
 * Represents the configuration for a path in the tower defense game.
 */
public class PathConfig {

	private String id;
	private List<PointConfig> points;

	/** Get the unique identifier of the path. */
	public String getId() {
		return id;
	}

	/** Get the list of points that make up the path. */
	public List<PointConfig> getPoints() {
		return points;
	}
}
