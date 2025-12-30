package com.towerdefense.editor.core.persistence;

import java.io.IOException;
import java.nio.file.Path;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.towerdefense.engine.api.model.configuration.LevelConfig;

public class LevelJsonIO {

    private final ObjectMapper mapper = new ObjectMapper();

    public void save(LevelConfig config, Path file) throws IOException {
        mapper.writerWithDefaultPrettyPrinter()
              .writeValue(file.toFile(), config);
    }

    public LevelConfig load(Path file) throws IOException {
        return mapper.readValue(file.toFile(), LevelConfig.class);
    }
}
