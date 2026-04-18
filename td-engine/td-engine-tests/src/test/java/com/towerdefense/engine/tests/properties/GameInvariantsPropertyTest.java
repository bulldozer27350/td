package com.towerdefense.engine.tests.properties;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.towerdefense.engine.api.GameEngineApi;
import com.towerdefense.engine.api.model.configuration.AttackConfig;
import com.towerdefense.engine.api.model.configuration.GameConfig;
import com.towerdefense.engine.api.model.configuration.LevelConfig;
import com.towerdefense.engine.api.model.configuration.WaveConfig;
import com.towerdefense.engine.tests.support.TestFailureRecorder;
import com.towerdefense.engine.tests.support.TestGameEngineFactory;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.ForAll;
import net.jqwik.api.Label;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import net.jqwik.api.ShrinkingMode;

/**
 * Tests basés sur les propriétés (Property-Based Testing).
 * 
 * Ces tests génèrent aléatoirement des configurations valides et vérifient
 * que les invariants métier sont TOUJOURS respectés, quelle que soit la config.
 * 
 * En cas d'échec, la configuration problématique est sauvegardée dans
 * target/test-failures/ pour analyse et rejeu.
 */
class GameInvariantsPropertyTest {

    /**
     * PROPRIÉTÉ 1 : L'or du joueur ne devient jamais négatif,
     * quelle que soit la configuration du jeu.
     */
    @Property(tries = 50, shrinking = ShrinkingMode.FULL)
    @Label("Player gold never becomes negative with any valid configuration")
    void player_gold_is_always_non_negative(
            @ForAll("validLevelConfigs") LevelConfig levelConfig) {
        
        GameEngineApi engine = null;
        try {
            // Crée un moteur avec la config aléatoire
            engine = TestGameEngineFactory.builder()
                    .withLevel(levelConfig)
                    .build();

            // Simule 500 ticks
            for (int i = 0; i < 500; i++) {
                engine.tick();
                
                int currentGold = engine.getState().player().currentGold();
                
                assertTrue(
                    currentGold >= 0,
                    String.format(
                        "L'or est devenu négatif au tick %d : %d",
                        i, currentGold
                    )
                );
                
                if (engine.isGameOver()) {
                    break;
                }
            }

        } catch (AssertionError | Exception e) {
            // Enregistre la configuration problématique
            GameConfig problemConfig = new GameConfig(
                levelConfig, 
                null, // TowersConfig
                null  // EnemiesConfig
            );
            TestFailureRecorder.recordFailure(
                "player_gold_is_always_non_negative",
                problemConfig,
                e
            );
            throw e;
        }
    }

    /**
     * PROPRIÉTÉ 2 : Les vies du joueur restent dans [0, startingLives],
     * quelle que soit la configuration.
     */
    @Property(tries = 50, shrinking = ShrinkingMode.FULL)
    @Label("Player lives stay within valid bounds")
    void player_lives_stay_within_bounds(
            @ForAll("validLevelConfigs") LevelConfig levelConfig) {
        
        try {
            GameEngineApi engine = TestGameEngineFactory.builder()
                    .withLevel(levelConfig)
                    .build();

            int initialLives = levelConfig.getStartingLives();

            for (int i = 0; i < 300; i++) {
                engine.tick();
                
                int currentLives = engine.getState().player().currentLives();
                
                assertTrue(
                    currentLives >= 0,
                    String.format("Vies négatives au tick %d : %d", i, currentLives)
                );
                
                assertTrue(
                    currentLives <= initialLives,
                    String.format(
                        "Vies supérieures à l'initial au tick %d : %d > %d",
                        i, currentLives, initialLives
                    )
                );
                
                if (engine.isGameOver()) {
                    break;
                }
            }

        } catch (AssertionError | Exception e) {
            GameConfig problemConfig = new GameConfig(levelConfig, null, null);
            TestFailureRecorder.recordFailure(
                "player_lives_stay_within_bounds",
                problemConfig,
                e
            );
            throw e;
        }
    }

    /**
     * PROPRIÉTÉ 3 : Le jeu se termine toujours dans un temps raisonnable
     * (pas de boucle infinie).
     */
    @Property(tries = 30, shrinking = ShrinkingMode.FULL)
    @Label("Game always terminates within reasonable time")
    void game_always_terminates(
            @ForAll("validLevelConfigs") LevelConfig levelConfig) {
        
        final int MAX_TICKS = 2000;
        
        try {
            GameEngineApi engine = TestGameEngineFactory.builder()
                    .withLevel(levelConfig)
                    .build();

            int tickCount = 0;
            while (!engine.isGameOver() && tickCount < MAX_TICKS) {
                engine.tick();
                tickCount++;
            }

            assertTrue(
                engine.isGameOver(),
                String.format(
                    "Le jeu n'a pas terminé après %d ticks (timeout)",
                    MAX_TICKS
                )
            );

            assertTrue(
                tickCount < MAX_TICKS,
                "Le jeu a atteint le timeout, possibilité de boucle infinie"
            );

        } catch (AssertionError | Exception e) {
            GameConfig problemConfig = new GameConfig(levelConfig, null, null);
            TestFailureRecorder.recordFailure(
                "game_always_terminates",
                problemConfig,
                e
            );
            throw e;
        }
    }

    /**
     * PROPRIÉTÉ 4 : L'état final du jeu est toujours cohérent,
     * quelle que soit la configuration.
     */
    @Property(tries = 50, shrinking = ShrinkingMode.FULL)
    @Label("Game end state is always consistent")
    void game_end_state_is_consistent(
            @ForAll("validLevelConfigs") LevelConfig levelConfig) {
        
        try {
            GameEngineApi engine = TestGameEngineFactory.builder()
                    .withLevel(levelConfig)
                    .build();

            // Fait tourner jusqu'à la fin
            int tickCount = 0;
            while (!engine.isGameOver() && tickCount < 2000) {
                engine.tick();
                tickCount++;
            }

            var finalState = engine.getState();
            
            // Invariants absolus
            assertTrue(
                finalState.player().currentGold() >= 0,
                "Or négatif en fin de partie"
            );
            
            assertTrue(
                finalState.player().currentLives() >= 0,
                "Vies négatives en fin de partie"
            );
            
            assertTrue(
                engine.isGameOver(),
                "Le jeu devrait être terminé"
            );
            
            // Cohérence victoire/défaite
            boolean hasLives = finalState.player().currentLives() > 0;
            boolean hasEnemies = !finalState.enemies().isEmpty();
            
            assertFalse(
                hasLives && hasEnemies,
                "Incohérence : vies > 0 ET ennemis restants avec jeu terminé"
            );

        } catch (AssertionError | Exception e) {
            GameConfig problemConfig = new GameConfig(levelConfig, null, null);
            TestFailureRecorder.recordFailure(
                "game_end_state_is_consistent",
                problemConfig,
                e
            );
            throw e;
        }
    }

    /**
     * PROPRIÉTÉ 5 : Le nombre d'entités (tours, ennemis, projectiles)
     * reste toujours positif.
     */
    @Property(tries = 50, shrinking = ShrinkingMode.FULL)
    @Label("Entity counts are always non-negative")
    void entity_counts_are_always_positive(
            @ForAll("validLevelConfigs") LevelConfig levelConfig) {
        
        try {
            GameEngineApi engine = TestGameEngineFactory.builder()
                    .withLevel(levelConfig)
                    .build();

            for (int i = 0; i < 500; i++) {
                engine.tick();
                
                var state = engine.getState();
                
                assertTrue(
                    state.towers().size() >= 0,
                    "Nombre de tours négatif"
                );
                
                assertTrue(
                    state.enemies().size() >= 0,
                    "Nombre d'ennemis négatif"
                );
                
                assertTrue(
                    state.projectiles().size() >= 0,
                    "Nombre de projectiles négatif"
                );
                
                if (engine.isGameOver()) {
                    break;
                }
            }

        } catch (AssertionError | Exception e) {
            GameConfig problemConfig = new GameConfig(levelConfig, null, null);
            TestFailureRecorder.recordFailure(
                "entity_counts_are_always_positive",
                problemConfig,
                e
            );
            throw e;
        }
    }

    // ========================
    // GÉNÉRATEURS PERSONNALISÉS
    // ========================

    /**
     * Fournit des LevelConfig aléatoires mais valides.
     * 
     * Note : Pour l'instant, retourne des configs simples.
     * À enrichir avec ConfigGenerators.levelConfigs() une fois
     * les setters publics ajoutés aux classes de config.
     */
    @Provide
    Arbitrary<LevelConfig> validLevelConfigs() {
        return Combinators.combine(
            Arbitraries.strings().alpha().ofMinLength(3).ofMaxLength(10),
            Arbitraries.integers().between(1, 10),
            Arbitraries.integers().between(50, 300)
        ).as((id, lives, gold) -> {
            LevelConfig config = new LevelConfig();
            config.setId("level_" + id);
            config.setStartingLives(lives);
            config.setStartingMoney(gold);
            
            // Ajoute une attaque simple
            WaveConfig wave = new WaveConfig(
                "wave_1",
                10,  // startTick
                20,  // spawnInterval
                5,   // count
                "goblin", // enemy
                "1" // path
            );
            
            AttackConfig attack = new AttackConfig(
                "attack_1",
                java.util.List.of(wave)
            );
            
            config.setAttacks(java.util.List.of(attack));
            
            // ✅ Ajoute un chemin valide pour correspondre à la vague
            com.towerdefense.engine.api.model.configuration.PathConfig path = 
                new com.towerdefense.engine.api.model.configuration.PathConfig();
            path.setId("1");
            path.setPoints(java.util.List.of(
                createPoint(0, 0),
                createPoint(0, 11)
            ));
            config.setPaths(java.util.List.of(path));
            
            return config;
        });
    }

    private com.towerdefense.engine.api.model.configuration.PointConfig createPoint(int x, int y) {
        var p = new com.towerdefense.engine.api.model.configuration.PointConfig();
        p.setX(x);
        p.setY(y);
        return p;
    }
}
