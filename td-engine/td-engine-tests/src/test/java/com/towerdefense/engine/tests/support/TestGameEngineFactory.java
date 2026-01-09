package com.towerdefense.engine.tests.support;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.towerdefense.engine.api.GameEngineApi;
import com.towerdefense.engine.api.model.configuration.GameConfig;
import com.towerdefense.engine.api.model.configuration.LevelConfig;
import com.towerdefense.engine.api.model.configuration.PathsConfig;
import com.towerdefense.engine.api.model.configuration.TowersConfig;
import com.towerdefense.engine.api.model.configuration.EnemiesConfig;
import com.towerdefense.runner.config.loader.JsonConfigLoader;

/**
 * Fabrique de GameEngineApi pour les tests avec pattern Builder.
 *
 * Valeurs par défaut :
 * - levelPath: "levels/level-default.json"
 * - pathsPath: "paths/paths-default.json"
 * - towersPath: "towers/towers.json"
 * - enemiesPath: "enemies/enemies-default.json"
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
 * </pre>
 */
public final class TestGameEngineFactory {

    private static final JsonConfigLoader loader = new JsonConfigLoader();

    // Valeurs par défaut
    private String levelPath = "levels/level-1.json";
    private String pathsPath = "paths/paths-1.json";
    private String towersPath = "towers/towers.json";
    private String enemiesPath = "enemies/enemies.json";

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

    public TestGameEngineFactory withLevel(String levelPath) {
        if (levelPath == null || levelPath.trim().isEmpty()) {
            throw new IllegalArgumentException("Le chemin du level ne peut pas être null ou vide");
        }
        this.levelPath = levelPath;
        return this;
    }

    public TestGameEngineFactory withPaths(String pathsPath) {
        if (pathsPath == null || pathsPath.trim().isEmpty()) {
            throw new IllegalArgumentException("Le chemin des paths ne peut pas être null ou vide");
        }
        this.pathsPath = pathsPath;
        return this;
    }

    public TestGameEngineFactory withTowers(String towersPath) {
        if (towersPath == null || towersPath.trim().isEmpty()) {
            throw new IllegalArgumentException("Le chemin des towers ne peut pas être null ou vide");
        }
        this.towersPath = towersPath;
        return this;
    }

    public TestGameEngineFactory withEnemies(String enemiesPath) {
        if (enemiesPath == null || enemiesPath.trim().isEmpty()) {
            throw new IllegalArgumentException("Le chemin des enemies ne peut pas être null ou vide");
        }
        this.enemiesPath = enemiesPath;
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
        LevelConfig level = loader.load(this.levelPath, LevelConfig.class);
        PathsConfig paths = loader.load(this.pathsPath, PathsConfig.class);
        TowersConfig towers = loader.load(this.towersPath, TowersConfig.class);
        EnemiesConfig enemies = loader.load(this.enemiesPath, EnemiesConfig.class);

        GameConfig gameConfig = new GameConfig(level, paths, towers, enemies);

        // 3. Initialisation explicite du moteur
        engine.initialize(gameConfig);

        return engine;
    }
}