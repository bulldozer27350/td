package com.towerdefense.exception;

/**
 * Exception levée quand une tour est introuvable.
 */
public class TowerNotFoundException extends TowerException {
    public TowerNotFoundException(String towerId) {
        super(
            "TOWER_NOT_FOUND",
            String.format("Tower with id %s not found", towerId),
            towerId
        );
    }
}