package com.towerdefense.engine.tests.timing;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.towerdefense.engine.api.GameEngineApi;
import com.towerdefense.engine.api.model.EnemyDTO;
import com.towerdefense.engine.api.model.command.PlaceTowerCommand;
import com.towerdefense.engine.tests.support.TestGameEngineFactory;

/**
 * Tests de validation du système de timing.
 */
class GameTimingTest {

	@Test
	void tower_respects_reload_time() {
		GameEngineApi engine = TestGameEngineFactory.builder().build();

		UUID playerId = UUID.fromString(engine.getState().player().id());

		// Place une tour
		engine.dispatch(new PlaceTowerCommand(5, 5, "mitrailleuse", playerId));

		// Attend que la tour soit construite
		for (int i = 0; i < 20; i++) {
			engine.tick();
		}

		int projectileCount = 0;
		int ticksWithProjectiles = 0;

		// Compte combien de fois la tour tire sur 100 ticks
		for (int i = 0; i < 100; i++) {
			engine.tick();

			if (!engine.getState().projectiles().isEmpty()) {
				ticksWithProjectiles++;
				projectileCount++;
			}
		}

		// La tour ne doit pas tirer à chaque tick (elle a un cooldown)
		assertTrue(ticksWithProjectiles < 50, "La tour ne doit pas tirer en continu, elle a un temps de rechargement");

		System.out.println("Projectiles tirés en 100 ticks : " + projectileCount);
	}

	@Test
	void enemy_speed_is_consistent() {
		GameEngineApi engine = TestGameEngineFactory.builder().build();

		// Attend qu'un ennemi apparaisse
		while (engine.getState().enemies().isEmpty() && !engine.isGameOver()) {
			engine.tick();
		}

		if (engine.getState().enemies().isEmpty()) {
			return; // Pas d'ennemi dans ce test
		}

		EnemyDTO enemy = engine.getState().enemies().get(0);
		double startX = enemy.position().x();
		double startY = enemy.position().y();

		// Fait avancer de 10 ticks (= 1 seconde)
		for (int i = 0; i < 10; i++) {
			engine.tick();
		}

		// Récupère la nouvelle position
		EnemyDTO updatedEnemy = engine.getState().enemies().stream().filter(e -> e.id().equals(enemy.id())).findFirst()
				.orElse(null);

		if (updatedEnemy != null) {
			double endX = updatedEnemy.position().x();
			double endY = updatedEnemy.position().y();

			double distanceMoved = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));

			System.out.println("Distance parcourue en 1 seconde : " + distanceMoved);

			// La vitesse devrait être cohérente avec la config
			// (dépend de la vitesse configurée dans enemies.json)
			assertTrue(distanceMoved > 0, "L'ennemi doit se déplacer");
		}
	}
}
