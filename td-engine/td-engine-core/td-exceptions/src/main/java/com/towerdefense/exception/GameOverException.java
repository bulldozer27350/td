package com.towerdefense.exception;

/**
 * Exception levée quand une action est tentée sur un jeu terminé.
 */
public class GameOverException extends TowerDefenseException {
    public GameOverException() {
        super("GAME_OVER", "Cannot perform action: game is over");
    }
}