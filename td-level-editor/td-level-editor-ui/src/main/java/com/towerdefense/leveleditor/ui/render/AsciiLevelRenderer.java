package com.towerdefense.leveleditor.ui.render;

import com.towerdefense.editor.api.model.EditableLevel;
import com.towerdefense.editor.api.model.EditablePath;
import com.towerdefense.editor.api.model.PositionDefinition;

public class AsciiLevelRenderer {

	public String render(EditableLevel level) {

		int width = level.getMap().getWidth();
		int height = level.getMap().getHeight();

		char[][] grid = new char[height][width];

		// 1. Initialisation
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				grid[y][x] = '.';
			}
		}

		// 2. Chemins
		for (EditablePath path : level.getPaths()) {

			var points = path.points();

			for (int i = 0; i < points.size() - 1; i++) {
				PositionDefinition from = points.get(i);
				PositionDefinition to = points.get(i + 1);

				drawSegment(grid, from, to);
			}

			// Marque début et fin
			PositionDefinition start = points.get(0);
			PositionDefinition end = points.get(points.size() - 1);
			grid[start.y()][start.x()] = 'I';
			grid[end.y()][end.x()] = 'O';
		}

//        // 3. Capacités de tours
//        for (EditableTowerCapacity capacity : level.getTowerCapacities()) {
//            for (PositionDefinition p : capacity.allowedPositions()) {
//                grid[p.y()][p.x()] = 'T';
//            }
//        }

		// 4. Rendu final
		StringBuilder out = new StringBuilder();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				out.append(grid[y][x]).append(' ');
			}
			out.append('\n');
		}

		return out.toString();
	}

	private void drawSegment(char[][] grid, PositionDefinition from, PositionDefinition to) {

		int x1 = from.x();
		int y1 = from.y();
		int x2 = to.x();
		int y2 = to.y();

		if (x1 == x2) {
			if (y1 < y2) {
				// Segment vertical descendant
				for (int y = y1; y <= y2; y++) {
					grid[y][x1] = 'V';
				}
			} else {
				// Segment vertical ascendant
				for (int y = y2; y <= y1; y++) {
					grid[y][x1] = '^';
				}
			}
			// Segment vertical
		} else if (y1 == y2) {
			if (x1 < x2) {
				// Segment horizontal droit
				for (int x = x1; x <= x2; x++) {
					grid[y1][x] = '>';
				}
			} else {
				// Segment horizontal gauche
				for (int x = x2; x <= x1; x++) {
					grid[y1][x] = '<';
				}
			}
		} else {
			throw new IllegalArgumentException("Diagonal segments are not supported: " + from + " -> " + to);
		}
	}
}
