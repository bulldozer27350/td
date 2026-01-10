package com.towerdefense.exception;

/**
 * Exception levée quand une action est tentée sur un jeu non initialisé.
 */
public class GameNotInitializedException extends TowerDefenseException {
    public GameNotInitializedException() {
        super("GAME_NOT_INITIALIZED", "Game has not been initialized");
    }
}
