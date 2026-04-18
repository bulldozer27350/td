package com.towerdefense.engine.tests.support;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.towerdefense.engine.api.GameEngineApi;
import com.towerdefense.engine.api.model.configuration.EnemiesConfig;
import com.towerdefense.engine.api.model.configuration.GameConfig;
import com.towerdefense.engine.api.model.configuration.LevelConfig;
import com.towerdefense.engine.api.model.configuration.TowersConfig;

/**
 * Fabrique de GameEngineApi pour les tests avec pattern Builder.
 * 
 * Cette factory scanne dynamiquement le dossier 'exportables/' pour charger
 * les configurations de tours, d'ennemis et de niveaux.
 */
public final class TestGameEngineFactory {

    private String levelId = "niveau_01"; // ID par défaut
    private LevelConfig directLevelConfig = null;
    private TowersConfig directTowersConfig = null;
    private EnemiesConfig directEnemiesConfig = null;

    public TestGameEngineFactory() {
    }

    public static TestGameEngineFactory builder() {
        return new TestGameEngineFactory();
    }

    public TestGameEngineFactory withLevel(String levelId) {
        this.levelId = levelId;
        this.directLevelConfig = null;
        return this;
    }

    public TestGameEngineFactory withLevel(LevelConfig config) {
        this.directLevelConfig = config;
        this.levelId = null;
        return this;
    }

    public TestGameEngineFactory withTowers(TowersConfig config) {
        this.directTowersConfig = config;
        return this;
    }

    public TestGameEngineFactory withEnemies(EnemiesConfig config) {
        this.directEnemiesConfig = config;
        return this;
    }

    /**
     * Construit et initialise le GameEngineApi avec la configuration découverte.
     */
    public GameEngineApi build() {
        // 1. Contexte Spring minimal
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("com.towerdefense");
        GameEngineApi engine = context.getBean(GameEngineApi.class);

        // 2. Chargement dynamique
        GameDataScanner scanner = new GameDataScanner();
        
        try {
            // A. Niveau
            LevelConfig level = loadLevel(scanner);
            
            // B. Tours (Agrégation de tous les fichiers de exportables/towers)
            TowersConfig towers = (directTowersConfig != null) ? directTowersConfig 
                                : aggregateTowers(scanner);
            
            // C. Ennemis (Agrégation de tous les fichiers de exportables/enemies)
            EnemiesConfig enemies = (directEnemiesConfig != null) ? directEnemiesConfig 
                                  : aggregateEnemies(scanner);

            GameConfig gameConfig = new GameConfig(level, towers, enemies);

            // 3. Initialisation du moteur
            engine.initialize(gameConfig);
            
            return engine;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'initialisation du moteur de test", e);
        }
    }

    private LevelConfig loadLevel(GameDataScanner scanner) throws java.io.IOException {
        if (directLevelConfig != null) return directLevelConfig;
        
        var allLevels = scanner.loadAllLevels();
        if (allLevels.isEmpty()) throw new IllegalStateException("Aucun niveau trouvé dans exportables/levels");
        
        return allLevels.stream()
                .filter(l -> l.getId().equalsIgnoreCase(levelId))
                .findFirst()
                .orElse(allLevels.get(0)); // Prend le premier par défaut si ID non trouvé
    }

    private TowersConfig aggregateTowers(GameDataScanner scanner) throws java.io.IOException {
        TowersConfig config = new TowersConfig();
        config.setTowers(scanner.loadAllTowers());
        if (config.getTowers().isEmpty()) {
            System.err.println("[WARNING] Aucune tour chargée depuis exportables/towers");
        }
        return config;
    }

    private EnemiesConfig aggregateEnemies(GameDataScanner scanner) throws java.io.IOException {
        EnemiesConfig config = new EnemiesConfig();
        config.setEnemies(scanner.loadAllEnemies());
        if (config.getEnemies().isEmpty()) {
            System.err.println("[WARNING] Aucun ennemi chargé depuis exportables/enemies");
        }
        return config;
    }
}
