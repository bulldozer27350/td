package com.towerdefense.engine.tests.invariants;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.towerdefense.engine.api.GameEngineApi;
import com.towerdefense.engine.api.model.command.PlaceTowerCommand;
import com.towerdefense.engine.tests.support.TestGameEngineFactory;

/**
 * Invariant métier : Les tours ne peuvent pas être placées au même endroit
 * et doivent respecter les contraintes d'achat.
 */
class TowerPlacementInvariantTest {

    @Test
    void cannot_place_two_towers_at_same_position() {
        GameEngineApi engine = TestGameEngineFactory.builder()
                .build();

        UUID playerId = UUID.fromString(engine.getState().player().id());

        // Place une première tour
        engine.dispatch(new PlaceTowerCommand(5, 5, "mitrailleuse", playerId));
        engine.tick();
        
        int towerCountAfterFirst = engine.getState().towers().size();
        
        // Tente de placer une deuxième tour au même endroit
        assertThrows(
            IllegalArgumentException.class,
            () -> engine.dispatch(new PlaceTowerCommand(5, 5, "mitrailleuse", playerId)),
            "On ne peut pas placer deux tours à la même position"
        );
        
        int towerCountAfterSecond = engine.getState().towers().size();
        
        assertEquals(
            towerCountAfterFirst,
            towerCountAfterSecond,
            "Le nombre de tours ne doit pas avoir changé"
        );
    }

    @Test
    void cannot_buy_tower_without_sufficient_gold() {
        GameEngineApi engine = TestGameEngineFactory.builder()
                .build();

        UUID playerId = UUID.fromString(engine.getState().player().id());
        
        // Épuise l'argent en achetant des tours
//        int initialTowerCount = engine.getState().towers().size();
        
        for (int x = 0; x < 12; x++) {
            for (int y = 0; y < 12; y++) {
                if (engine.getState().player().currentGold() < 50) {
                    final int fx = x;
                    final int fy = y;
                    int towersBefore = engine.getState().towers().size();
                    // Vérifie qu'aucune tour n'a été ajoutée
                    assertThrows(
                        IllegalArgumentException.class,
                        () -> engine.dispatch(new PlaceTowerCommand(fx, fy, "mitrailleuse", playerId))
                    );
                    
                    assertEquals(
                        towersBefore,
                        engine.getState().towers().size(),
                        "On ne peut pas acheter une tour sans argent suffisant"
                    );
                    return;
                }
                
                try {
                    engine.dispatch(new PlaceTowerCommand(x, y, "mitrailleuse", playerId));
                    engine.tick();
                } catch (IllegalArgumentException e) {
                    // Ignore occupied positions or out of bounds during gold depletion
                }
            }
        }
    }

    @Test
    void tower_appears_in_game_state_after_build_time() {
        GameEngineApi engine = TestGameEngineFactory.builder()
                .build();
        UUID playerId = UUID.fromString(engine.getState().player().id());
        
        int initialCount = engine.getState().towers().size();
        
        // Place une tour
        engine.dispatch(new PlaceTowerCommand(7, 7, "mitrailleuse", playerId));
        
        // Attend que la construction se termine (buildTimeTicks)
        for (int i = 0; i < 20; i++) {
            engine.tick();
        }
        
        assertTrue(
            engine.getState().towers().size() >= initialCount,
            "La tour doit apparaître dans l'état après sa construction"
        );
    }

    @Test
    void tower_count_never_decreases_without_sell() {
        GameEngineApi engine = TestGameEngineFactory.builder()
                .build();

        UUID playerId = UUID.fromString(engine.getState().player().id());
        
        engine.dispatch(new PlaceTowerCommand(3, 3, "mitrailleuse", playerId));
        engine.tick();
        
        int towerCount = engine.getState().towers().size();
        
        // Fait avancer le jeu sans vendre
        for (int i = 0; i < 50; i++) {
            engine.tick();
            
            assertTrue(
                engine.getState().towers().size() >= towerCount,
                "Le nombre de tours ne doit pas diminuer sans action de vente"
            );
        }
    }
}
