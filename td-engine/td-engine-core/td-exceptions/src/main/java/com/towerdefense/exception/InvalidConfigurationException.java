package com.towerdefense.exception;

/**
 * Exception levée quand une configuration est invalide.
 */
public class InvalidConfigurationException extends TowerDefenseException {
    private final String configType;
    
    public InvalidConfigurationException(String configType, String message) {
        super(
            "INVALID_CONFIG",
            String.format("Invalid %s configuration: %s", configType, message)
        );
        this.configType = configType;
    }
    
    public String getConfigType() { return configType; }
}