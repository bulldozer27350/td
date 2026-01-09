package com.towerdefense.engine.tests.generators;

import com.towerdefense.engine.api.model.configuration.*;
import net.jqwik.api.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Générateurs de configurations aléatoires mais valides pour les tests property-based.
 * 
 * Chaque générateur respecte les contraintes métier du jeu.
 */
public class ConfigGenerators {

    // ========================
    // CONSTANTES DE DOMAINE
    // ========================
    
    private static final int MIN_STARTING_GOLD = 50;
    private static final int MAX_STARTING_GOLD = 500;
    
    private static final int MIN_STARTING_LIVES = 1;
    private static final int MAX_STARTING_LIVES = 20;
    
    private static final int MIN_ENEMY_HP = 10;
    private static final int MAX_ENEMY_HP = 500;
    
    private static final double MIN_ENEMY_SPEED = 0.1;
    private static final double MAX_ENEMY_SPEED = 5.0;
    
    private static final int MIN_BOUNTY = 1;
    private static final int MAX_BOUNTY = 100;
    
    private static final int MIN_TOWER_COST = 10;
    private static final int MAX_TOWER_COST = 200;
    
    private static final double MIN_TOWER_RANGE = 1.0;
    private static final double MAX_TOWER_RANGE = 8.0;
    
    private static final int MIN_TOWER_DAMAGE = 5;
    private static final int MAX_TOWER_DAMAGE = 100;
    
    private static final double MIN_RELOAD_TIME = 0.5;
    private static final double MAX_RELOAD_TIME = 5.0;
    
    private static final int MIN_BUILD_TIME = 1;
    private static final int MAX_BUILD_TIME = 50;

    // ========================
    // GÉNÉRATEURS
    // ========================

    /**
     * Génère un EnemyTypeConfig aléatoire mais valide.
     */
    @Provide
    public Arbitrary<EnemyTypeConfig> enemyTypes() {
        return Combinators.combine(
            Arbitraries.strings().alpha().ofMinLength(3).ofMaxLength(10),
            Arbitraries.integers().between(MIN_ENEMY_HP, MAX_ENEMY_HP),
            Arbitraries.doubles().between(MIN_ENEMY_SPEED, MAX_ENEMY_SPEED),
            Arbitraries.integers().between(MIN_BOUNTY, MAX_BOUNTY)
        ).as((id, hp, speed, bounty) -> {
            EnemyTypeConfig config = new EnemyTypeConfig();
            config.setId("enemy_" + id);
            config.setHp(hp);
            config.setSpeed(speed);
            config.setBounty(bounty);
            return config;
        });
    }

    /**
     * Génère une liste d'EnemyTypeConfig.
     */
    @Provide
    public Arbitrary<List<EnemyTypeConfig>> enemyTypesList() {
        return enemyTypes().list().ofMinSize(1).ofMaxSize(5);
    }

    /**
     * Génère un EnemiesConfig complet.
     */
    @Provide
    public Arbitrary<EnemiesConfig> enemiesConfig() {
        return enemyTypesList().map(enemies -> {
            EnemiesConfig config = new EnemiesConfig();
            // Note: EnemiesConfig n'a pas de setter public, on doit utiliser reflection
            // ou créer une classe builder dédiée
            // Pour l'instant, on retourne une config vide à enrichir
            return config;
        });
    }

    /**
     * Génère un TowerLevelConfig aléatoire mais valide.
     */
    @Provide
    public Arbitrary<TowerLevelConfig> towerLevels() {
        return Combinators.combine(
            Arbitraries.integers().between(1, 5),
            Arbitraries.integers().between(MIN_TOWER_COST, MAX_TOWER_COST),
            Arbitraries.integers().between(MIN_TOWER_COST / 2, MAX_TOWER_COST),
            Arbitraries.doubles().between(MIN_TOWER_RANGE, MAX_TOWER_RANGE),
            Arbitraries.integers().between(MIN_TOWER_DAMAGE, MAX_TOWER_DAMAGE),
            Arbitraries.doubles().between(MIN_RELOAD_TIME, MAX_RELOAD_TIME),
            Arbitraries.integers().between(MIN_BUILD_TIME, MAX_BUILD_TIME)
        ).as((level, cost, sell, range, damage, reload, buildTime) -> {
            // Contrainte métier : sellValue < upgradeCost
            int sellValue = Math.min(sell, cost - 1);
            
            return new TowerLevelConfig(
                level,
                cost,
                sellValue,
                range,
                damage,
                reload,
                buildTime
            );
        });
    }

    /**
     * Génère un TowerTypeConfig avec plusieurs niveaux cohérents.
     */
    @Provide
    public Arbitrary<TowerTypeConfig> towerTypes() {
        return Combinators.combine(
            Arbitraries.strings().alpha().ofMinLength(3).ofMaxLength(10),
            Arbitraries.integers().between(1, 3)
        ).flatAs((id, levelCount) -> {
            // Génère des niveaux avec coûts croissants
            Arbitrary<TowerLevelConfig> towerLevels = towerLevels();
			return towerLevels.list().ofSize(levelCount)
                .map(levelsList -> {
                    TowerTypeConfig config = new TowerTypeConfig();
                    config.setId("tower_" + id);
                    config.setName("Tower " + id);
                    
                    // Assure que les niveaux ont des coûts croissants
                    List<TowerLevelConfig> sortedLevels = new ArrayList<>();
                    int previousCost = MIN_TOWER_COST;
                    int level = 1;
                    
                    for (TowerLevelConfig originalLevel : levelsList) {
                        int newCost = Math.max(previousCost + 10, originalLevel.getUpgradeCost());
                        int sellValue = (int) (newCost * 0.7); // 70% du coût
                        
                        sortedLevels.add(new TowerLevelConfig(
                            level++,
                            newCost,
                            sellValue,
                            originalLevel.getRange(),
                            originalLevel.getDamage(),
                            originalLevel.getReloadSeconds(),
                            originalLevel.getBuildTimeTicks()
                        ));
                        
                        previousCost = newCost;
                    }
                    
                    config.getLevels().addAll(sortedLevels);
                    return config;
                });
        });
    }

    /**
     * Génère un PointConfig pour les chemins.
     */
    @Provide
    public Arbitrary<PointConfig> points() {
        return Combinators.combine(
            Arbitraries.integers().between(0, 14),
            Arbitraries.integers().between(0, 14)
        ).as((x, y) -> {
            PointConfig point = new PointConfig();
            // Note: PointConfig n'a pas de setters publics
            // Il faudrait ajouter un constructeur ou des setters
            return point;
        });
    }

    /**
     * Génère un PathConfig valide (chemin avec au moins 2 points).
     */
    @Provide
    public Arbitrary<PathConfig> paths() {
        return Combinators.combine(
            Arbitraries.strings().alpha().ofMinLength(3).ofMaxLength(10),
            points().list().ofMinSize(2).ofMaxSize(8)
        ).as((id, pointsList) -> {
            PathConfig path = new PathConfig();
            // Note: PathConfig n'a pas de setters publics
            return path;
        });
    }

    /**
     * Génère un WaveConfig aléatoire mais valide.
     */
    @Provide
    public Arbitrary<WaveConfig> waves() {
        return Combinators.combine(
            Arbitraries.strings().alpha().ofMinLength(3).ofMaxLength(10),
            Arbitraries.integers().between(0, 100),
            Arbitraries.integers().between(5, 30),
            Arbitraries.integers().between(1, 20),
            Arbitraries.strings().alpha().ofMinLength(3).ofMaxLength(10),
            Arbitraries.strings().alpha().ofMinLength(3).ofMaxLength(10)
        ).as((id, startTick, interval, count, enemyType, pathId) -> 
            new WaveConfig(
                "wave_" + id,
                startTick,
                interval,
                count,
                "enemy_" + enemyType,
                "path_" + pathId
            )
        );
    }

    /**
     * Génère un AttackConfig avec plusieurs vagues.
     */
    @Provide
    public Arbitrary<AttackConfig> attacks() {
        return Combinators.combine(
            Arbitraries.strings().alpha().ofMinLength(3).ofMaxLength(10),
            waves().list().ofMinSize(1).ofMaxSize(5)
        ).as((id, wavesList) -> 
            new AttackConfig("attack_" + id, wavesList)
        );
    }

    /**
     * Génère un LevelConfig complet et cohérent.
     */
    @Provide
    public Arbitrary<LevelConfig> levelConfigs() {
        return Combinators.combine(
            Arbitraries.strings().alpha().ofMinLength(3).ofMaxLength(10),
            attacks().list().ofMinSize(1).ofMaxSize(3),
            Arbitraries.integers().between(MIN_STARTING_LIVES, MAX_STARTING_LIVES),
            Arbitraries.integers().between(MIN_STARTING_GOLD, MAX_STARTING_GOLD)
        ).as((id, attacksList, lives, gold) -> {
            LevelConfig config = new LevelConfig();
            config.setId("level_" + id);
            config.setAttacks(attacksList);
            config.setStartingLives(lives);
            config.setStartingMoney(gold);
            return config;
        });
    }

    /**
     * Génère un GameConfig complet avec toutes les dépendances cohérentes.
     */
    @Provide
    public Arbitrary<GameConfig> gameConfigs() {
        return Combinators.combine(
            levelConfigs(),
            paths().list().ofMinSize(1).ofMaxSize(3),
            towerTypes().list().ofMinSize(1).ofMaxSize(3),
            enemyTypesList()
        ).as((level, pathsList, towersList, enemiesList) -> {
            // Crée les configs intermédiaires
            PathsConfig pathsConfig = new PathsConfig();
            // Note: PathsConfig n'a pas de setter public
            
            TowersConfig towersConfig = new TowersConfig();
            // Note: TowersConfig n'a pas de setter public
            
            EnemiesConfig enemiesConfig = new EnemiesConfig();
            // Note: EnemiesConfig n'a pas de setter public
            
            // Pour l'instant, retourne une config avec seulement le level
            // Il faudra enrichir avec les autres configs
            return new GameConfig(level, pathsConfig, towersConfig, enemiesConfig);
        });
    }
}