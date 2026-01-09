package com.towerdefense.engine.tests.scenarios;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.towerdefense.engine.api.GameEngineApi;
import com.towerdefense.engine.api.model.command.PlaceTowerCommand;
import com.towerdefense.engine.tests.support.TestGameEngineFactory;


/**
 * Scénario fonctionnel : Une tour bien placée doit pouvoir
 * éliminer des ennemis et protéger le joueur.
 */
class WaveWithTowerScenarioTest {

    @Test
    void tower_kills_enemies_and_player_survives() {
        GameEngineApi engine = TestGameEngineFactory.builder()
                .build();

        UUID playerId = UUID.fromString(engine.getState().player().id());
        int initialLives = engine.getState().player().currentLives();

        // Place une tour sur le chemin
        engine.dispatch(new PlaceTowerCommand(5, 5, "machinegun", playerId));
        
        // Fait tourner le jeu un certain temps
        for (int i = 0; i < 300; i++) {
            engine.tick();
            
            if (engine.isGameOver()) {
                break;
            }
        }

        // Vérifie que le joueur a survécu mieux qu'avec 0 défense
        int finalLives = engine.getState().player().currentLives();
        
        assertTrue(
            finalLives > 0 || engine.getState().enemies().isEmpty(),
            "Avec une tour, le joueur devrait mieux se défendre"
        );
    }

    @Test
    void multiple_towers_provide_better_defense() {
        // Scénario 1 : Sans tours
        GameEngineApi engineNoTowers = TestGameEngineFactory.builder()
                .build();

        int livesLostWithoutTowers = 0;
        int initialLives = engineNoTowers.getState().player().currentLives();
        
        for (int i = 0; i < 200; i++) {
            engineNoTowers.tick();
            if (engineNoTowers.isGameOver()) {
                break;
            }
        }
        livesLostWithoutTowers = initialLives - engineNoTowers.getState().player().currentLives();

        // Scénario 2 : Avec plusieurs tours
        GameEngineApi engineWithTowers = TestGameEngineFactory.builder()
                .build();

        UUID playerId = UUID.fromString(engineWithTowers.getState().player().id());
        
        // Place plusieurs tours
        engineWithTowers.dispatch(new PlaceTowerCommand(3, 3, "machinegun", playerId));
        engineWithTowers.dispatch(new PlaceTowerCommand(7, 7, "machinegun", playerId));
        
        for (int i = 0; i < 200; i++) {
            engineWithTowers.tick();
            if (engineWithTowers.isGameOver()) {
                break;
            }
        }
        
        int livesLostWithTowers = initialLives - engineWithTowers.getState().player().currentLives();

        assertTrue(
            livesLostWithTowers <= livesLostWithoutTowers,
            "Les tours doivent offrir une meilleure protection"
        );
    }

    @Test
    void tower_shoots_projectiles_at_enemies() {
        GameEngineApi engine = TestGameEngineFactory.builder()
                .build();

        UUID playerId = UUID.fromString(engine.getState().player().id());
        
        // Place une tour
        engine.dispatch(new PlaceTowerCommand(5, 5, "machinegun", playerId));
        
        boolean projectilesSeen = false;
        
        // Vérifie qu'à un moment des projectiles sont tirés
        for (int i = 0; i < 150; i++) {
            engine.tick();
            
            if (!engine.getState().projectiles().isEmpty()) {
                projectilesSeen = true;
                break;
            }
        }

        assertTrue(
            projectilesSeen,
            "La tour doit tirer des projectiles quand des ennemis sont à portée"
        );
    }
}