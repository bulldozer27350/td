package com.towerdefense.viewer;

import java.util.ArrayList;
import java.util.List;

import com.towerdefense.engine.api.model.EnemyDTO;
import com.towerdefense.engine.api.model.GameObject;
import com.towerdefense.engine.api.model.GameStateDTO;
import com.towerdefense.engine.api.model.LevelMapDTO;
import com.towerdefense.engine.api.model.PositionDTO;
import com.towerdefense.engine.api.model.ProjectileDTO;
import com.towerdefense.engine.api.model.TowerDTO;
import com.towerdefense.engine.api.model.WayDTO;

public class GameStateAsciiRenderer {
	private final RendererRegistry registry;
	private final int cellWidth = 7;
	private final int cellHeight = 3;
	private int width;
	private int height;
	private LevelMapDTO currentMap;

	public GameStateAsciiRenderer(RendererRegistry registry) {
		this.registry = registry;
	}

	public String print(GameStateDTO state) {
		StringBuilder out = new StringBuilder();

		// Header
		out.append("Level ").append(state.player().progress().levelIndex() + 1);
		out.append(" | Attack ").append(state.player().progress().attackIndex() + 1);
		out.append("\n");
		out.append("Player ").append(state.player().id());
		out.append(" $$$: ").append(state.player().currentGold());
		out.append(" ❤️: ").append(state.player().currentLives());
		out.append("\n\n");

		// Ligne de coordonnées X (header)
		out.append("     "); // Espace pour la colonne Y (maintenant 5 espaces pour Y sur 2 chiffres)
		for (int x = 0; x < width; x++) {
			out.append(String.format("%-" + cellWidth + "s", "  [" + x + "]"));
		}
		out.append("\n");

		// Ligne séparatrice
		out.append("    ╔");
		for (int x = 0; x < width; x++) {
			out.append("═".repeat(cellWidth - 1));
			out.append(x < width - 1 ? "╤" : "╗");
		}
		out.append("\n");

		// Contenu du plateau
		for (int y = 0; y < height; y++) {
			// Préparer les cellules pour cette ligne
			String[][][] rowCells = new String[width][][];
			for (int x = 0; x < width; x++) {
				PositionDTO pos = new PositionDTO(x, y);
				GameObject obj = this.objectAt(state, pos);

				if (obj != null) {
					// Un objet est présent (tour, ennemi, projectile)
					Renderer renderer = registry.getRenderer(obj);
					rowCells[x] = renderer != null ? renderer.render(obj) : emptyCell();
				} else {
					// Vérifier si c'est sur le chemin
					PathDirection dir = getPathDirection(pos);
					if (dir != null) {
						rowCells[x] = pathCell(dir);
					} else {
						rowCells[x] = emptyCell();
					}
				}
			}

			// Afficher les cellHeight sous-lignes
			for (int sub = 0; sub < cellHeight; sub++) {
				// Numéro de ligne (Y) sur la première sous-ligne, avec padding pour 2 chiffres
				if (sub == 1) {
					out.append(String.format("[%2d]║", y)); // %2d pour aligner sur 2 chiffres
				} else {
					out.append("    ║"); // 4 espaces pour aligner avec [XX]║
				}

				// Contenu des cellules
				for (int x = 0; x < width; x++) {
					String content = rowCells[x][sub][0];
					// Centrer le contenu dans la cellule
					out.append(String.format("%-" + (cellWidth - 1) + "s", content));
					out.append(x < width - 1 ? "│" : "║");
				}
				out.append("\n");
			}

			// Ligne séparatrice entre les rangées
			if (y < height - 1) {
				out.append("    ╟");
				for (int x = 0; x < width; x++) {
					out.append("─".repeat(cellWidth - 1));
					out.append(x < width - 1 ? "┼" : "╢");
				}
				out.append("\n");
			}
		}

		// Ligne de fermeture
		out.append("    ╚");
		for (int x = 0; x < width; x++) {
			out.append("═".repeat(cellWidth - 1));
			out.append(x < width - 1 ? "╧" : "╝");
		}
		out.append("\n");

		return out.toString();
	}

	/**
	 * Retourne la direction du chemin à cette position, ou null si pas sur le
	 * chemin. Interpole les positions manquantes entre les waypoints.
	 */
	private PathDirection getPathDirection(PositionDTO pos) {
		if (currentMap == null || currentMap.paths() == null || currentMap.paths().isEmpty()) {
			return null;
		}

		List<WayDTO> ways = currentMap.paths();
		
		// Construire le chemin complet avec interpolation
		List<PositionDTO> fullPath = new ArrayList<>();
		for (WayDTO way : ways) {
			List<PositionDTO> wayPoints = way.positions();
			if (wayPoints.isEmpty()) continue;
			
			// Ajouter le premier point
			fullPath.add(wayPoints.get(0));
			
			// Interpoler entre chaque paire de waypoints
			for (int i = 0; i < wayPoints.size() - 1; i++) {
				PositionDTO current = wayPoints.get(i);
				PositionDTO next = wayPoints.get(i + 1);
				
				// Ajouter toutes les positions intermédiaires
				List<PositionDTO> interpolated = interpolate(current, next);
				fullPath.addAll(interpolated);
			}
		}

		// Trouver la position dans le chemin complet
		for (int i = 0; i < fullPath.size(); i++) {
			PositionDTO pathPos = fullPath.get(i);
			if (pathPos.x() == pos.x() && pathPos.y() == pos.y()) {
				// Position trouvée, déterminer la direction
				if (i < fullPath.size() - 1) {
					PositionDTO next = fullPath.get(i + 1);
					int dx = (int) (next.x() - pos.x());
					int dy = (int) (next.y() - pos.y());

					if (dx > 0) return PathDirection.RIGHT;
					if (dx < 0) return PathDirection.LEFT;
					if (dy > 0) return PathDirection.DOWN;
					if (dy < 0) return PathDirection.UP;
				}
				// Dernière position du chemin
				return PathDirection.END;
			}
		}

		return null;
	}

	/**
	 * Interpole toutes les positions entre start et end (exclus start, inclus end)
	 */
	private List<PositionDTO> interpolate(PositionDTO start, PositionDTO end) {
		List<PositionDTO> positions = new ArrayList<>();
		
		int dx = (int) (end.x() - start.x());
		int dy = (int) (end.y() - start.y());
		
		// Normaliser la direction
		int stepX = Integer.compare(dx, 0);
		int stepY = Integer.compare(dy, 0);
		
		int currentX = (int) start.x() + stepX;
		int currentY = (int) start.y() + stepY;
		
		// Ajouter toutes les positions jusqu'à end (inclus)
		while (currentX != end.x() || currentY != end.y()) {
			positions.add(new PositionDTO(currentX, currentY));
			
			if (currentX != end.x()) {
				currentX += stepX;
			}
			if (currentY != end.y()) {
				currentY += stepY;
			}
		}
		
		// Ajouter la position finale
		positions.add(new PositionDTO((int)end.x(), (int)end.y()));
		
		return positions;
	}

	/** Cellule de chemin avec flèche directionnelle */
	private String[][] pathCell(PathDirection dir) {
		String arrow = switch (dir) {
		case UP -> "  ↑  ";
		case DOWN -> "  ↓  ";
		case LEFT -> "  ←  ";
		case RIGHT -> "  →  ";
		case END -> "  ⊗  "; // Symbole pour la fin
		};

		return new String[][] { { "······" }, { arrow }, { "······" } };
	}

	/** Cellule vide */
	private String[][] emptyCell() {
		return new String[][] { { "      " }, { "      " }, { "      " } };
	}

	/** Returns the game object at the specified position, or null if none exists */
	private GameObject objectAt(GameStateDTO state, PositionDTO pos) {
		// Towers (priorité la plus haute)
		for (TowerDTO t : state.towers()) {
			if (t.position().equals(pos)) {
				return t;
			}
		}

		// Enemies
		for (EnemyDTO e : state.enemies()) {
			int ex = (int) Math.round(e.position().x());
			int ey = (int) Math.round(e.position().y());
			if (pos.x() == ex && pos.y() == ey) {
				return e;
			}
		}

		// Projectiles
		for (ProjectileDTO p : state.projectiles()) {
			int px = (int) Math.round(p.position().x());
			int py = (int) Math.round(p.position().y());
			if (pos.x() == px && pos.y() == py) {
				return p;
			}
		}

		return null;
	}

	public void setMap(LevelMapDTO levelMap) {
		this.width = levelMap.dimensions().width();
		this.height = levelMap.dimensions().height();
		this.currentMap = levelMap;
	}

	private enum PathDirection {
		UP, DOWN, LEFT, RIGHT, END
	}
}