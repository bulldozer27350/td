package com.towerdefense.exception;

/**
 * Exception levée quand le joueur n'a plus de vies.
 */
public class NoLivesRemainingException extends TowerDefenseException {
    public NoLivesRemainingException() {
        super("NO_LIVES", "Player has no lives remaining");
    }
}
