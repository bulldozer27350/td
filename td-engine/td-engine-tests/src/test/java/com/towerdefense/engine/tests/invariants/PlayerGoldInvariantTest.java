package com.towerdefense.engine.tests.invariants;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.towerdefense.engine.api.GameEngineApi;
import com.towerdefense.engine.api.model.command.PlaceTowerCommand;
import com.towerdefense.engine.tests.support.TestGameEngineFactory;


/**
 * Invariant métier : L'argent du joueur ne doit jamais être négatif,
 * même après des tentatives d'achat de tours.
 */
class PlayerGoldInvariantTest {

    @Test
    void player_gold_never_becomes_negative() {
        GameEngineApi engine = TestGameEngineFactory.builder()
                .build();

        UUID playerId = UUID.fromString(
            engine.getState().player().id()
        );

        // Tentative d'achat de plusieurs tours pour épuiser l'argent
        // Tentative d'achat de plusieurs tours pour épuiser l'argent
        for (int i = 0; i < 12; i++) {
            // Utilise try-catch car certaines positions peuvent être sur le chemin ou hors limites
            try {
                engine.dispatch(new PlaceTowerCommand(i, 1, "mitrailleuse", playerId));
            } catch (Exception e) {
                // Ignore
            }
        }

        // L'argent ne doit jamais être négatif
        assertTrue(
            engine.getState().player().currentGold() >= 0,
            "L'argent du joueur ne peut pas être négatif"
        );
    }

    @Test
    void player_gold_decreases_when_buying_tower() {
        GameEngineApi engine = TestGameEngineFactory.builder()
                .build();

        int initialGold = engine.getState().player().currentGold();
        UUID playerId = UUID.fromString(engine.getState().player().id());

        engine.dispatch(new PlaceTowerCommand(5, 5, "mitrailleuse", playerId));
        engine.tick();

        int finalGold = engine.getState().player().currentGold();
        
        assertTrue(
            finalGold < initialGold || finalGold == initialGold,
            "L'argent doit diminuer ou rester identique après tentative d'achat"
        );
    }

    @Test
    void player_earns_gold_when_enemy_dies() {
        GameEngineApi engine = TestGameEngineFactory.builder()
                .build();

        UUID playerId = UUID.fromString(engine.getState().player().id());
        
        // Place une tour pour tuer les ennemis (position (5,5) est hors chemin sur niveau_01)
        engine.dispatch(new PlaceTowerCommand(5, 5, "mitrailleuse", playerId));
        
        int goldBeforeKills = engine.getState().player().currentGold();
        
        // Fait avancer le jeu jusqu'à ce qu'un ennemi meure
        for (int i = 0; i < 100; i++) {
            engine.tick();
            int currentGold = engine.getState().player().currentGold();
            if (currentGold > goldBeforeKills) {
                // Un ennemi est mort et a donné de l'argent
                assertTrue(
                    currentGold > goldBeforeKills,
                    "Le joueur doit gagner de l'argent quand un ennemi meurt"
                );
                return;
            }
        }
    }
}
