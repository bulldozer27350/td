package com.towerdefense.config.loader;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;

public class JsonConfigLoader {

    private final ObjectMapper objectMapper = new ObjectMapper();

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

