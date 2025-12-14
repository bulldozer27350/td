package com.towerdefense.viewer;

public interface Renderer<T> {
    /**
     * Retourne une cellule ASCII composée de cellHeight lignes
     * et chaque ligne contient cellWidth caractères (ex. 5).
     */
    String[][] render(T obj);
}
