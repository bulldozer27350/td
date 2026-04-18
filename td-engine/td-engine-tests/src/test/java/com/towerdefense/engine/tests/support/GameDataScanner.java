package com.towerdefense.engine.tests.support;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.towerdefense.engine.api.model.configuration.EnemyTypeConfig;
import com.towerdefense.engine.api.model.configuration.LevelConfig;
import com.towerdefense.engine.api.model.configuration.TowerTypeConfig;

/**
 * Utilitaire pour scanner et charger dynamiquement les fichiers de configuration
 * depuis le répertoire 'exportables/'.
 */
public class GameDataScanner {
    private final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final Path rootPath;

    public GameDataScanner() {
        this.rootPath = findExportablesPath();
        System.out.println("[TEST] Scanned exportables path: " + rootPath);
    }

    private Path findExportablesPath() {
        // Remonte les parents depuis le répertoire courant pour trouver "exportables"
        Path current = Paths.get("").toAbsolutePath();
        while (current != null) {
            Path exportables = current.resolve("exportables");
            if (Files.exists(exportables) && Files.isDirectory(exportables)) {
                return exportables;
            }
            current = current.getParent();
        }
        // Fallback pour les environnements de build où le répertoire courant peut varier
        // On essaie de voir si on est dans un sous-module
        return Paths.get("../../exportables").toAbsolutePath();
    }

    public List<LevelConfig> loadAllLevels() throws IOException {
        return loadFiles(rootPath.resolve("levels"), LevelConfig.class);
    }

    public List<TowerTypeConfig> loadAllTowers() throws IOException {
        return loadFiles(rootPath.resolve("towers"), TowerTypeConfig.class);
    }

    public List<EnemyTypeConfig> loadAllEnemies() throws IOException {
        return loadFiles(rootPath.resolve("enemies"), EnemyTypeConfig.class);
    }

    private <T> List<T> loadFiles(Path dir, Class<T> type) throws IOException {
        if (!Files.exists(dir)) {
            System.err.println("[TEST] Directory not found: " + dir);
            return new ArrayList<>();
        }
        
        List<T> results = new ArrayList<>();
        try (Stream<Path> stream = Files.list(dir)) {
            stream.filter(p -> p.toString().endsWith(".json"))
                  .forEach(p -> {
                      try {
                          results.add(mapper.readValue(p.toFile(), type));
                      } catch (IOException e) {
                          System.err.println("[TEST] Failed to load " + p + ": " + e.getMessage());
                      }
                  });
        }
        return results;
    }
}
