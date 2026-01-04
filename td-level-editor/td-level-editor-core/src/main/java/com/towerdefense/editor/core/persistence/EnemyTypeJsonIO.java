package com.towerdefense.editor.core.persistence;

import java.io.IOException;
import java.nio.file.Path;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.towerdefense.engine.api.model.configuration.EnemyTypeConfig;

public class EnemyTypeJsonIO {

    private final ObjectMapper mapper = new ObjectMapper();

    public void save(EnemyTypeConfig config, Path file) throws IOException {
        mapper.writerWithDefaultPrettyPrinter()
              .writeValue(file.toFile(), config);
    }

    public EnemyTypeConfig load(Path file) throws IOException {
        return mapper.readValue(file.toFile(), EnemyTypeConfig.class);
    }
}
