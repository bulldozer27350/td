package com.towerdefense.editor.api.model;

import java.util.List;

/**
 * High-level definition of a path used in a level. This definition is
 * editor-facing and engine-agnostic.
 */
public record PathDefinition(String id, List<PositionDefinition> points) {
	public PathDefinition {
		if (id == null || id.isBlank()) {
			throw new IllegalArgumentException("Path id must not be null or blank");
		}
		if (points == null || points.size() < 2) {
			throw new IllegalArgumentException("A path must contain at least two points");
		}
	}
}
