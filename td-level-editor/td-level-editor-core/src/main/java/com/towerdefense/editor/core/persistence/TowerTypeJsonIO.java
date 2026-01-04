package com.towerdefense.editor.core.persistence;

import java.io.IOException;
import java.nio.file.Path;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.towerdefense.engine.api.model.configuration.TowerTypeConfig;

public class TowerTypeJsonIO {

    private final ObjectMapper mapper = new ObjectMapper();

    public void save(TowerTypeConfig config, Path file) throws IOException {
        mapper.writerWithDefaultPrettyPrinter()
              .writeValue(file.toFile(), config);
    }

    public TowerTypeConfig load(Path file) throws IOException {
        return mapper.readValue(file.toFile(), TowerTypeConfig.class);
    }
}
