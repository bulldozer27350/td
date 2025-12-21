package com.towerdefense.viewer;

import com.towerdefense.domain.GameObject;
import com.towerdefense.domain.GameState;
import com.towerdefense.domain.Position;
import com.towerdefense.domain.dynamik.level.LevelProgress;

public class GameStateAsciiRenderer {

    private final RendererRegistry registry;

    private final int cellWidth = 5;
    private final int cellHeight = 3;

    public GameStateAsciiRenderer(RendererRegistry registry) {
        this.registry = registry;
    }

    public String print(GameState state) {

        int width = state.gridWidth();
        int height = state.gridHeight();

        StringBuilder out = new StringBuilder();
        
        LevelProgress p = state.levelProgress();

        out.append("Level ");
        out.append(p.levelIndex() + 1);
        out.append(" | Attack ");
        out.append(p.attackIndex() + 1);
        out.append("\n");
        
        out.append("Player ");
        out.append(state.player().id().value().toString());
        out.append(" $$$: ");
        out.append(state.player().gold());
        out.append(" ❤️: ");
        out.append(state.player().lives());
        out.append("\n");

        for (int y = 0; y < height; y++) {

            // Chaque ligne du plateau a cellHeight sous-lignes
            String[][][] rowCells = new String[width][][];
            for (int x = 0; x < width; x++) {

                GameObject obj = state.objectAt(new Position(x, y));

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

    private String[][] emptyCell() {
        return new String[][]{
            {"     "},
            {"     "},
            {"     "}
        };
    }
}
