package com.towerdefense.progression.loader;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.towerdefense.progression.domain.player.PlayerProgress;
import com.towerdefense.progression.model.EnemyTypeData;

import com.towerdefense.progression.model.LevelData;
import com.towerdefense.progression.model.TowerTypeData;

public class DataLoader {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Path dataDirectory;
    
    public DataLoader(Path dataDirectory) {
        this.dataDirectory = dataDirectory;
    }
    
    public List<LevelData> loadLevels() throws IOException {
        Path levelsDir = dataDirectory.resolve("levels");
        return loadJsonFiles(levelsDir, LevelData.class);
    }
    
    public List<TowerTypeData> loadTowers() throws IOException {
        Path towersDir = dataDirectory.resolve("towers");
        return loadJsonFiles(towersDir, TowerTypeData.class);
    }
    
    public List<EnemyTypeData> loadEnemies() throws IOException {
        Path enemiesDir = dataDirectory.resolve("enemies");
        return loadJsonFiles(enemiesDir, EnemyTypeData.class);
    }

    public PlayerProgress loadPlayerProgress(String clientId) throws IOException {
        Path profilePath = dataDirectory.resolve("profiles").resolve(clientId + ".json");
        if (!Files.exists(profilePath)) {
            return new PlayerProgress();
        }
        return objectMapper.readValue(profilePath.toFile(), PlayerProgress.class);
    }

    public void savePlayerProgress(String clientId, PlayerProgress progress) throws IOException {
        Path profilesDir = dataDirectory.resolve("profiles");
        if (!Files.exists(profilesDir)) {
            Files.createDirectories(profilesDir);
        }
        Path profilePath = profilesDir.resolve(clientId + ".json");
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(profilePath.toFile(), progress);
    }
    
    private <T> List<T> loadJsonFiles(Path directory, Class<T> type) throws IOException {

        if (!Files.exists(directory)) {
            return List.of();
        }
        
        List<T> results = new ArrayList<>();
        try (Stream<Path> files = Files.list(directory)) {
            files.filter(p -> p.toString().endsWith(".json"))
                 .forEach(path -> {
                     try {
                         T obj = objectMapper.readValue(path.toFile(), type);
                         results.add(obj);
                     } catch (IOException e) {
                         throw new UncheckedIOException(e);
                     }
                 });
        }
        return results;
    }
}
