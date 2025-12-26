package com.towerdefense.orchestrator.runtime;

import java.util.List;

import com.towerdefense.domain.GameState;
import com.towerdefense.domain.dynamik.enemy.Enemy;

/**
 * Represents an attack consisting of multiple enemy waves in the tower defense
 * game.
 */
public class Attack {

	private final List<EnemyWave> waves;
	private AttackState state = AttackState.WAITING;

	private Integer startTick = null;

	/** Constructor to initialize the attack with given enemy waves. */
	public Attack(List<EnemyWave> waves) {
		this.waves = waves;
	}

	/**
	 * Advances the attack state based on the global tick and spawns enemies as
	 * needed.
	 */
	public void tick(GameState gameState, int globalTick) {

		if (this.state == AttackState.FINISHED)
			return;

		if (this.state == AttackState.WAITING) {
			this.state = AttackState.RUNNING;
			this.startTick = globalTick; // 🔑 référence temporelle
		}

		int attackTick = globalTick - this.startTick;

		for (EnemyWave wave : this.waves) {
			for (Enemy enemy : wave.dueSpawns(attackTick)) {
				gameState.addEnemy(enemy);
			}
		}

		if (this.waves.stream().allMatch(EnemyWave::isFinished) && gameState.enemies().isEmpty()) {
			this.state = AttackState.FINISHED;
		}
	}

	/** Checks if the attack has finished. */
	public boolean isFinished() {
		return this.state == AttackState.FINISHED;
	}

}
