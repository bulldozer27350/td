package com.towerdefense.orchestrator.runtime;

import java.util.List;

import com.towerdefense.domain.GameState;
import com.towerdefense.domain.dynamik.level.LevelProgress;

/**
 * Represents a level scenario in the tower defense game, managing a sequence of attacks.
 */
public class LevelScenario {

	private final List<Attack> attacks;
	private final String levelId;
	private int currentAttack = 0;

	/** Constructor to initialize level scenario with level ID and list of attacks. */
	public LevelScenario(String levelId, List<Attack> attacks) {
		this.levelId = levelId;
		this.attacks = attacks;
	}

	/** Progress the level scenario by one tick. */
	public void tick(GameState state, int tick) {
		if (isFinished())
			return;

		Attack attack = attacks.get(currentAttack);
		attack.tick(state, tick);

		if (attack.isFinished()) {
			currentAttack++;
		}
	}
	
	/** Check if the level scenario has finished all attacks. */
	public boolean isFinished() {
		return currentAttack >= attacks.size();
	}

	/** Get the unique identifier of the level. */
	public String levelId() {
		return levelId;
	}
	
	/** Create a snapshot of the current level progress. */
	public LevelProgress snapshot() {
	    return new LevelProgress(
	        levelId,
	        currentAttack
	    );
	}
}