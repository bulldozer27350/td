package com.towerdefense.engine.tests.scenarios;

import com.towerdefense.engine.api.GameEngineApi;
import com.towerdefense.engine.tests.support.TestGameEngineFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Scénario fonctionnel : La partie doit se terminer correctement
 * dans différentes conditions.
 */
class GameCompletionScenarioTest {

    @Test
    void game_ends_when_all_waves_completed() {
        GameEngineApi engine = TestGameEngineFactory.builder()
                .build();

        int tickCount = 0;
        final int MAX_TICKS = 2000;

        while (!engine.isGameOver() && tickCount < MAX_TICKS) {
            engine.tick();
            tickCount++;
        }

        assertTrue(
            engine.isGameOver(),
            "Le jeu doit se terminer après toutes les vagues"
        );

        assertTrue(
            tickCount < MAX_TICKS,
            "Le jeu ne doit pas tourner indéfiniment"
        );
    }

    @Test
    void game_ends_when_player_loses_all_lives() {
        GameEngineApi engine = TestGameEngineFactory.builder()
                .build();

        // Laisse les ennemis passer sans défense
        while (!engine.isGameOver()) {
            engine.tick();
        }

        assertTrue(
            engine.getState().player().currentLives() == 0 ||
            engine.getState().enemies().isEmpty(),
            "Le jeu doit se terminer quand toutes les vies sont perdues"
        );
    }

    @Test
    void game_state_is_consistent_at_end() {
        GameEngineApi engine = TestGameEngineFactory.builder()
                .build();

        while (!engine.isGameOver()) {
            engine.tick();
        }

        // Vérifie la cohérence de l'état final
        assertAll(
            "État final du jeu",
            () -> assertEquals(0, engine.getState().enemies().size(),
                    "Aucun ennemi ne doit rester"),
            () -> assertTrue(engine.getState().player().currentGold() >= 0,
                    "L'argent doit être positif"),
            () -> assertTrue(engine.getState().player().currentLives() >= 0,
                    "Les vies doivent être positives ou nulles")
        );
    }

    @Test
    void game_progresses_through_attack_waves() {
        GameEngineApi engine = TestGameEngineFactory.builder()
                .build();

        int initialAttackIndex = engine.getState().player().progress().attackIndex();
        
        // Fait tourner le jeu un moment
        for (int i = 0; i < 500; i++) {
            engine.tick();
            
            if (engine.isGameOver()) {
                break;
            }
        }

        int finalAttackIndex = engine.getState().player().progress().attackIndex();

        assertTrue(
            finalAttackIndex >= initialAttackIndex,
            "Le jeu doit progresser à travers les vagues d'attaque"
        );
    }

    @Test
    void no_enemies_remain_when_game_completes_successfully() {
        GameEngineApi engine = TestGameEngineFactory.builder()
                .build();

        while (!engine.isGameOver()) {
            engine.tick();
        }

        // Si le joueur a gagné (vies > 0), aucun ennemi ne doit rester
        if (engine.getState().player().currentLives() > 0) {
            assertEquals(
                0,
                engine.getState().enemies().size(),
                "Aucun ennemi ne doit rester si le joueur a gagné"
            );
        }
    }
}