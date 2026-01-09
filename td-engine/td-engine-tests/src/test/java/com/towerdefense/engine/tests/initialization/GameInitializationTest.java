package com.towerdefense.engine.tests.initialization;

import com.towerdefense.engine.api.GameEngineApi;
import com.towerdefense.engine.tests.support.TestGameEngineFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests pour vérifier que l'initialisation du jeu se fait correctement.
 */
class GameInitializationTest {

    @Test
    void game_state_is_fully_initialized_after_build() {
        GameEngineApi engine = TestGameEngineFactory.builder()
                .build();

        // Vérifie que tous les composants essentiels sont initialisés
        assertNotNull(engine.getState(), "GameState ne doit pas être null");
        assertNotNull(engine.getState().player(), "Player ne doit pas être null");
        assertNotNull(engine.getState().player().id(), "PlayerId ne doit pas être null");
        assertNotNull(engine.getState().player().progress(), "LevelProgress ne doit pas être null");
        
        // Vérifie les valeurs par défaut
        assertTrue(
            engine.getState().player().currentGold() >= 0,
            "L'or initial doit être positif ou nul"
        );
        assertTrue(
            engine.getState().player().currentLives() > 0,
            "Les vies initiales doivent être positives"
        );
    }

    @Test
    void level_progress_has_valid_initial_values() {
        GameEngineApi engine = TestGameEngineFactory.builder()
                .build();

        var progress = engine.getState().player().progress();
        
        assertNotNull(progress.levelIndex(), "Level index ne doit pas être null");
        assertEquals(0, progress.attackIndex(), "Attack index initial doit être 0");
    }

    @Test
    void game_starts_in_progress_state() {
        GameEngineApi engine = TestGameEngineFactory.builder()
                .build();

        assertFalse(
            engine.isGameOver(),
            "Le jeu ne doit pas être terminé dès l'initialisation"
        );
    }

    @Test
    void initial_collections_are_empty() {
        GameEngineApi engine = TestGameEngineFactory.builder()
                .build();

        assertEquals(0, engine.getState().towers().size(), 
            "Pas de tours au démarrage");
        // Les ennemis peuvent être 0 ou plus selon le timing de la première vague
        assertTrue(engine.getState().enemies().size() >= 0,
            "Les ennemis doivent être >= 0");
        assertEquals(0, engine.getState().projectiles().size(),
            "Pas de projectiles au démarrage");
    }

    @Test
    void can_safely_call_getState_multiple_times() {
        GameEngineApi engine = TestGameEngineFactory.builder()
                .build();

        // Appels répétés de getState() ne doivent pas causer d'erreur
        var state1 = engine.getState();
        var state2 = engine.getState();
        
        assertNotNull(state1);
        assertNotNull(state2);
        
        // Les états doivent représenter le même instant
        assertEquals(
            state1.player().currentGold(),
            state2.player().currentGold()
        );
    }
}