package com.towerdefense.exception;

/**
 * Exception levée quand un chemin est inconnu.
 */
public class UnknownPathException extends InvalidConfigurationException {
    private final String pathId;
    
    public UnknownPathException(String pathId) {
        super(
            "path",
            String.format("Unknown path id: %s", pathId)
        );
        this.pathId = pathId;
    }
    
    public String getPathId() { return pathId; }
}