package com.towerdefense.exception;

/**
 * Exception levée quand un joueur tente une action qui n'est pas autorisée.
 */
public class UnauthorizedPlayerActionException extends TowerDefenseException {
    private final String playerId;
    private final String action;
    
    public UnauthorizedPlayerActionException(String playerId, String action) {
        super(
            "UNAUTHORIZED_ACTION",
            String.format("Player %s is not authorized to perform action: %s", playerId, action)
        );
        this.playerId = playerId;
        this.action = action;
    }
    
    public String getPlayerId() { return playerId; }
    public String getAction() { return action; }
}
