package com.towerdefense.exception;

/**
 * Exception levée quand le LevelProgress n'est pas initialisé.
 */
public class LevelProgressNotInitializedException extends TowerDefenseException {
    public LevelProgressNotInitializedException() {
        super(
            "LEVEL_PROGRESS_NOT_INITIALIZED",
            "LevelProgress has not been initialized. Call setLevelProgress() before accessing the game state."
        );
    }
}
