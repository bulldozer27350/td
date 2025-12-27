package com.towerdefense.runner.config.loader;

import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utility class to load JSON configuration files from resources.
 */
public class JsonConfigLoader {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /** Loads a JSON configuration file from the given resource path and maps it to the specified type.
	 *
	 * @param resourcePath the path to the resource file
	 * @param type the class type to map
	 * @return the mapped configuration object
	 * @throws IllegalStateException if the resource is not found or mapping fails
	 */
    public <T> T load(String resourcePath, Class<T> type) {
        InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath);
        if (is == null) {
            throw new IllegalStateException("Config file not found: " + resourcePath);
        }

        try {
            return objectMapper.readValue(is, type);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load config: " + resourcePath, e);
        }
    }
}

