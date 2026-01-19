package com.towerdefense.engine.tests.support;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.towerdefense.engine.api.GameEngineApi;
import com.towerdefense.engine.api.model.configuration.EnemiesConfig;
import com.towerdefense.engine.api.model.configuration.GameConfig;
import com.towerdefense.engine.api.model.configuration.LevelConfig;
import com.towerdefense.engine.api.model.configuration.TowersConfig;
import com.towerdefense.runner.config.loader.JsonConfigLoader;

/**
 * Fabrique de GameEngineApi pour les tests avec pattern Builder.
 *
 * Valeurs par défaut :
 * - levelPath: "levels/level-1.json"
 * - pathsPath: "paths/paths-1.json"
 * - towersPath: "towers/towers.json"
 * - enemiesPath: "enemies/enemies.json"
 *
 * Exemple d'utilisation :
 * <pre>
 * // Utilise toutes les valeurs par défaut
 * GameEngineApi engine = TestGameEngineFactory.builder().build();
 * 
 * // Override certaines valeurs
 * GameEngineApi engine = TestGameEngineFactory.builder()
 *     .withLevel("configs/level1.json")
 *     .withPaths("configs/paths1.json")
 *     .build();
 *     
 * // ✅ NOUVEAU : Utilise une LevelConfig générée aléatoirement
 * GameEngineApi engine = TestGameEngineFactory.builder()
 *     .withLevel(randomLevelConfig)
 *     .build();
 * </pre>
 */
public final class TestGameEngineFactory {

    private static final JsonConfigLoader loader = new JsonConfigLoader();

    // Valeurs par défaut
    private String levelPath = "levels/level-1.json";
    private String towersPath = "towers/towers.json";
    private String enemiesPath = "enemies/enemies.json";
    
    // ✅ NOUVEAU : Support des configs directes (pour property-based testing)
    private LevelConfig directLevelConfig = null;
    private TowersConfig directTowersConfig = null;
    private EnemiesConfig directEnemiesConfig = null;

    /**
     * Constructeur public pour permettre l'instanciation directe.
     * Usage: new TestGameEngineFactory().withLevel(...).build()
     */
    public TestGameEngineFactory() {
    }

    /**
     * Méthode factory statique (style plus fluent/idiomatique).
     * Usage: TestGameEngineFactory.builder().withLevel(...).build()
     */
    public static TestGameEngineFactory builder() {
        return new TestGameEngineFactory();
    }

    // ========================
    // CONFIGURATION PAR CHEMIN (existant)
    // ========================

    public TestGameEngineFactory withLevel(String levelPath) {
        if (levelPath == null || levelPath.trim().isEmpty()) {
            throw new IllegalArgumentException("Le chemin du level ne peut pas être null ou vide");
        }
        this.levelPath = levelPath;
        this.directLevelConfig = null; // Reset le mode direct
        return this;
    }

    public TestGameEngineFactory withPaths(String pathsPath) {
        if (pathsPath == null || pathsPath.trim().isEmpty()) {
            throw new IllegalArgumentException("Le chemin des paths ne peut pas être null ou vide");
        }
        return this;
    }

    public TestGameEngineFactory withTowers(String towersPath) {
        if (towersPath == null || towersPath.trim().isEmpty()) {
            throw new IllegalArgumentException("Le chemin des towers ne peut pas être null ou vide");
        }
        this.towersPath = towersPath;
        this.directTowersConfig = null;
        return this;
    }

    public TestGameEngineFactory withEnemies(String enemiesPath) {
        if (enemiesPath == null || enemiesPath.trim().isEmpty()) {
            throw new IllegalArgumentException("Le chemin des enemies ne peut pas être null ou vide");
        }
        this.enemiesPath = enemiesPath;
        this.directEnemiesConfig = null;
        return this;
    }

    // ========================
    // ✅ NOUVEAU : CONFIGURATION DIRECTE (pour property-based testing)
    // ========================

    /**
     * Utilise une LevelConfig directement (au lieu de charger depuis un fichier).
     * Utile pour les tests property-based avec configs générées aléatoirement.
     */
    public TestGameEngineFactory withLevel(LevelConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("LevelConfig ne peut pas être null");
        }
        this.directLevelConfig = config;
        this.levelPath = null;
        return this;
    }

    /**
     * Utilise une TowersConfig directement.
     */
    public TestGameEngineFactory withTowers(TowersConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("TowersConfig ne peut pas être null");
        }
        this.directTowersConfig = config;
        this.towersPath = null;
        return this;
    }

    /**
     * Utilise une EnemiesConfig directement.
     */
    public TestGameEngineFactory withEnemies(EnemiesConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("EnemiesConfig ne peut pas être null");
        }
        this.directEnemiesConfig = config;
        this.enemiesPath = null;
        return this;
    }

    /**
     * Construit et initialise le GameEngineApi avec la configuration fournie.
     * 
     * @return GameEngineApi initialisé et prêt à l'emploi
     */
    public GameEngineApi build() {
        // 1. Contexte Spring minimal, basé sur la config réelle du moteur
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext("com.towerdefense");

        GameEngineApi engine = context.getBean(GameEngineApi.class);

        // 2. Chargement de la configuration de jeu
        LevelConfig level = loadLevelConfig();
        TowersConfig towers = loadTowersConfig();
        EnemiesConfig enemies = loadEnemiesConfig();

        GameConfig gameConfig = new GameConfig(level, towers, enemies);

        // 3. Initialisation explicite du moteur
        engine.initialize(gameConfig);

        return engine;
    }

    // ========================
    // MÉTHODES PRIVÉES DE CHARGEMENT
    // ========================

    private LevelConfig loadLevelConfig() {
        if (directLevelConfig != null) {
            return directLevelConfig;
        }
        LevelConfig levelConfig = loader.load(levelPath, LevelConfig.class);
        return levelConfig;
    }

    private TowersConfig loadTowersConfig() {
        if (directTowersConfig != null) {
            return directTowersConfig;
        }
        return loader.load(towersPath, TowersConfig.class);
    }

    private EnemiesConfig loadEnemiesConfig() {
        if (directEnemiesConfig != null) {
            return directEnemiesConfig;
        }
        return loader.load(enemiesPath, EnemiesConfig.class);
    }
}