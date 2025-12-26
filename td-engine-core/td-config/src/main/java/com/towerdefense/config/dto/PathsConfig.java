package com.towerdefense.config.dto;

import java.util.List;

/**
 * Represents the configuration for paths in the tower defense game.
 */
public class PathsConfig {

    private List<PathConfig> paths;
    
    /** Get the list of path configurations. */
    public List<PathConfig> getPaths() {
        return paths;
    }
}
