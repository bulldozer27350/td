package com.towerdefense.leveleditor.http.bootstrap;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.towerdefense.editor.api.TowerDefenseEditorApi;
import com.towerdefense.leveleditor.http.config.EditorPathsConfiguration;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);
    
    private final TowerDefenseEditorApi editorApi;
    private final EditorPathsConfiguration pathsConfig;
    
    public DataLoader(TowerDefenseEditorApi editorApi, EditorPathsConfiguration pathsConfig) {
        this.editorApi = editorApi;
        this.pathsConfig = pathsConfig;
    }
    
    @Override
    public void run(String... args) {
        log.info("🚀 Démarrage du chargement automatique des données...");
        
        // Créer les répertoires s'ils n'existent pas
        createDirectoriesIfNeeded();
        
        // Charger les données dans l'ordre : enemies, towers, puis levels
        // (car les levels dépendent des enemies et towers)
        loadEnemies();
        loadTowers();
        loadLevels();
        
        log.info("✅ Chargement automatique terminé!");
        printSummary();
    }
    
    private void createDirectoriesIfNeeded() {
        createDirIfNotExists(pathsConfig.getDraftEnemiesPath());
        createDirIfNotExists(pathsConfig.getDraftTowersPath());
        createDirIfNotExists(pathsConfig.getDraftLevelsPath());
        createDirIfNotExists(pathsConfig.getExportableEnemiesPath());
        createDirIfNotExists(pathsConfig.getExportableTowersPath());
        createDirIfNotExists(pathsConfig.getExportableLevelsPath());
    }
    
    private void createDirIfNotExists(String path) {
        try {
            Path dirPath = Paths.get(path);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
                log.info("📁 Répertoire créé: {}", path);
            }
        } catch (Exception e) {
            log.warn("⚠️ Impossible de créer le répertoire {}: {}", path, e.getMessage());
        }
    }
    
    private void loadEnemies() {
        log.info("👾 Chargement des types d'ennemis...");
        
        // Draft enemies
        loadFromDirectory(
            pathsConfig.getDraftEnemiesPath(),
            file -> {
                try {
                    editorApi.loadDraftEnemyType(file.getAbsolutePath());
                    log.debug("  ✓ Draft enemy chargé: {}", file.getName());
                    return true;
                } catch (Exception e) {
                    log.error("  ✗ Erreur lors du chargement de {}: {}", file.getName(), e.getMessage());
                    return false;
                }
            }
        );
        
        // Exportable enemies
        loadFromDirectory(
            pathsConfig.getExportableEnemiesPath(),
            file -> {
                try {
                    editorApi.loadExportableEnemyType(file.getAbsolutePath());
                    log.debug("  ✓ Exportable enemy chargé: {}", file.getName());
                    return true;
                } catch (Exception e) {
                    log.error("  ✗ Erreur lors du chargement de {}: {}", file.getName(), e.getMessage());
                    return false;
                }
            }
        );
    }
    
    private void loadTowers() {
        log.info("🗼 Chargement des types de tours...");
        
        // Draft towers
        loadFromDirectory(
            pathsConfig.getDraftTowersPath(),
            file -> {
                try {
                    editorApi.loadDraftTowerType(file.getAbsolutePath());
                    log.debug("  ✓ Draft tower chargé: {}", file.getName());
                    return true;
                } catch (Exception e) {
                    log.error("  ✗ Erreur lors du chargement de {}: {}", file.getName(), e.getMessage());
                    return false;
                }
            }
        );
        
        // Exportable towers
        loadFromDirectory(
            pathsConfig.getExportableTowersPath(),
            file -> {
                try {
                    editorApi.loadExportableTowerType(file.getAbsolutePath());
                    log.debug("  ✓ Exportable tower chargé: {}", file.getName());
                    return true;
                } catch (Exception e) {
                    log.error("  ✗ Erreur lors du chargement de {}: {}", file.getName(), e.getMessage());
                    return false;
                }
            }
        );
    }
    
    private void loadLevels() {
        log.info("🗺️ Chargement des niveaux...");
        
        // Draft levels
        loadFromDirectory(
            pathsConfig.getDraftLevelsPath(),
            file -> {
                try {
                    editorApi.loadDraftLevel(file.getAbsolutePath());
                    log.debug("  ✓ Draft rank chargé: {}", file.getName());
                    return true;
                } catch (Exception e) {
                    log.error("  ✗ Erreur lors du chargement de {}: {}", file.getName(), e.getMessage());
                    return false;
                }
            }
        );
        
        // Exportable levels
        loadFromDirectory(
            pathsConfig.getExportableLevelsPath(),
            file -> {
                try {
                    editorApi.loadExportableLevel(file.getAbsolutePath());
                    log.debug("  ✓ Exportable rank chargé: {}", file.getName());
                    return true;
                } catch (Exception e) {
                    log.error("  ✗ Erreur lors du chargement de {}: {}", file.getName(), e.getMessage());
                    return false;
                }
            }
        );
    }
    
    private void loadFromDirectory(String directoryPath, FileProcessor processor) {
        try {
            Path dir = Paths.get(directoryPath);
            
            if (!Files.exists(dir)) {
                log.debug("  ℹ️ Répertoire inexistant: {}", directoryPath);
                return;
            }
            
            try (Stream<Path> paths = Files.walk(dir, 1)) {
                long loaded = paths
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".json"))
                    .map(Path::toFile)
                    .filter(processor::process)
                    .count();
                
                log.info("  📦 {} fichier(s) chargé(s) depuis {}", loaded, directoryPath);
            }
            
        } catch (Exception e) {
            log.error("  ❌ Erreur lors du scan de {}: {}", directoryPath, e.getMessage());
        }
    }
    
    private void printSummary() {
        log.info("📊 Résumé du chargement:");
        log.info("  - Enemies: {}", editorApi.getAllEditableEnemyTypes().size());
        log.info("  - Towers: {}", editorApi.getAllEditableTowerTypes().size());
        log.info("  - Levels: {}", editorApi.getAllEditableLevels().size());
    }
    
    @FunctionalInterface
    private interface FileProcessor {
        boolean process(File file);
    }
}