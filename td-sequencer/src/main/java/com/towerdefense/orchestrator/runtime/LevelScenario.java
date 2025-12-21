package com.towerdefense.orchestrator.runtime;

import java.util.List;

import com.towerdefense.domain.GameState;
import com.towerdefense.domain.dynamik.level.LevelProgress;

public class LevelScenario {

	private final List<Attack> attacks;
	private final int levelId;
	private int currentAttack = 0;

	public LevelScenario(int levelId, List<Attack> attacks) {
		this.levelId = levelId;
		this.attacks = attacks;
	}

	public void tick(GameState state, int tick) {
		if (isFinished())
			return;

		Attack attack = attacks.get(currentAttack);
		attack.tick(state, tick);

		if (attack.isFinished()) {
			currentAttack++;
		}
	}

	public boolean isFinished() {
		return currentAttack >= attacks.size();
	}

	public int levelId() {
		return levelId;
	}
	
	public LevelProgress snapshot() {
	    return new LevelProgress(
	        levelId,
	        currentAttack
	    );
	}
}