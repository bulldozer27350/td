package com.towerdefense.orchestrator.runtime;

import java.util.List;

import com.towerdefense.domain.GameState;
import com.towerdefense.domain.dynamik.level.LevelProgress;
import com.towerdefense.domain.statik.level.TowerCapacityDefinition;

/**
 * Represents a level scenario in the tower defense game, managing a sequence of attacks.
 */
public class LevelScenario {

	private final List<Attack> attacks;
	private final String levelId;
	private int currentAttack = 0;
	private List<TowerCapacityDefinition> towerCapacities;

	/** Constructor to initialize level scenario with level ID and list of attacks. 
	 * @param towerCapacitiesDef */
	public LevelScenario(String levelId, List<Attack> attacks, List<TowerCapacityDefinition> towerCapacitiesDef) {
		this.levelId = levelId;
		this.attacks = attacks;
		this.towerCapacities = towerCapacitiesDef;
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

	public List<TowerCapacityDefinition> getTowerCapacities() {
		return towerCapacities;
	}
	
	
}