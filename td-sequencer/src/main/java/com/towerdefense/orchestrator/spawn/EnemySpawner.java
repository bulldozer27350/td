package com.towerdefense.orchestrator.spawn;

import java.util.ArrayDeque;
import java.util.Queue;

import com.towerdefense.domain.GameState;
import com.towerdefense.domain.enemy.Enemy;

/**
 * Gère le séquencement d'apparition des ennemis. Cette classe connaît les
 * différentes vagues à gérer et la carte pour gérer les déplacements des
 * ennemis
 */
public class EnemySpawner {

	private final Queue<EnemyWave> waves = new ArrayDeque<>();

	public void addWave(EnemyWave wave) {
		this.waves.add(wave);
	}

	public void tick(GameState state, int tick) {
		// Tant qu'il reste des vagues à arriver
		if (!this.waves.isEmpty()) {
			EnemyWave current = this.waves.peek();
			// sur la vague courante, pour chaque ennemi à faire apparaître
			for (Enemy spawn : current.dueSpawns(tick)) {
				// On ajoute l'ennemi au jeu
				state.addEnemy(spawn);
			}
			// Une fois la vague terminée, on la supprime pour que le prochain appel soit
			// correctement câblé.
			if (current.isFinished()) {
				this.waves.poll();
			}
		}
	}
}
