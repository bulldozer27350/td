package com.towerdefense.exception;

/**
 * Exception levée quand une position est déjà occupée par une tour.
 */
public class PositionOccupiedException extends TowerException {
    private final int x;
    private final int y;
    
    public PositionOccupiedException(int x, int y, String existingTowerId) {
        super(
            "POSITION_OCCUPIED",
            String.format("Position (%d, %d) is already occupied by tower %s", x, y, existingTowerId),
            existingTowerId
        );
        this.x = x;
        this.y = y;
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
}