package com.towerdefense.viewer;

import com.towerdefense.engine.api.model.EnemyDTO;
import com.towerdefense.engine.api.model.GameObject;
import com.towerdefense.engine.api.model.GameStateDTO;
import com.towerdefense.engine.api.model.LevelMapDTO;
import com.towerdefense.engine.api.model.PositionDTO;
import com.towerdefense.engine.api.model.ProjectileDTO;
import com.towerdefense.engine.api.model.TowerDTO;

public class GameStateAsciiRenderer {

    private final RendererRegistry registry;

    private final int cellWidth = 5;
    private final int cellHeight = 3;
    private int width;
    private int height;
    
    public GameStateAsciiRenderer(RendererRegistry registry) {
        this.registry = registry;
    }

    public String print(GameStateDTO state) {
        StringBuilder out = new StringBuilder();
        
        out.append("Level ");
        out.append(state.player().progress().levelIndex() + 1);
        out.append(" | Attack ");
        out.append(state.player().progress().attackIndex() + 1);
        out.append("\n");
        
        out.append("Player ");
        out.append(state.player().id());
        out.append(" $$$: ");
        out.append(state.player().currentGold());
        out.append(" ❤️: ");
        out.append(state.player().currentLives());
        out.append("\n");

        for (int y = 0; y < height; y++) {

            // Chaque ligne du plateau a cellHeight sous-lignes
            String[][][] rowCells = new String[width][][];
            for (int x = 0; x < width; x++) {

                GameObject obj = this.objectAt(state, new PositionDTO(x, y));

                Renderer renderer = (obj == null)
                    ? null
                    : registry.getRenderer(obj);

                if (renderer == null) {
                    rowCells[x] = emptyCell();
                } else {
                    rowCells[x] = renderer.render(obj);
                }
            }

            // Imprime les cellHeight sous-lignes
            for (int sub = 0; sub < cellHeight; sub++) {
                for (int x = 0; x < width; x++) {
                    out.append(rowCells[x][sub][0]);
                }
                out.append("\n");
            }
        }
        return out.toString();
    }
    
    /** Returns the game object at the specified position, or null if none exists */
	private GameObject objectAt(GameStateDTO state, PositionDTO pos) {

		// Towers
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

    private String[][] emptyCell() {
        return new String[][]{
            {"     "},
            {"     "},
            {"     "}
        };
    }

	public void setMap(LevelMapDTO levelMap) {
		this.width = levelMap.dimensions().width();
		this.height = levelMap.dimensions().height();
	}
}
