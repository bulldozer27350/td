package com.towerdefense.orchestrator.runtime;

import java.util.ArrayList;
import java.util.List;

import com.towerdefense.domain.dynamik.enemy.Enemy;
import com.towerdefense.domain.map.EnemyPath;
import com.towerdefense.domain.statik.enemy.EnemyFactory;

/**
 * Represents a wave of enemies in the tower defense game.
 */
public class EnemyWave {

	private final int startTick;
	private final int interval;
	private final int count;
	private final int waveId;

	private final EnemyFactory factory;
	private final EnemyPath path;

	private int spawned = 0;

	/** Constructor to initialize the enemy wave with its parameters. */
	public EnemyWave(int waveId, int startTick, int interval, int count, EnemyFactory factory, EnemyPath path) {
		this.waveId = waveId;
		this.startTick = startTick;
		this.interval = interval;
		this.count = count;
		this.factory = factory;
		this.path = path;
	}

	/** Determines which enemies are due to spawn at the given tick. */
	public List<Enemy> dueSpawns(int currentTick) {
		List<Enemy> result = new ArrayList<>();

		if (currentTick < startTick)
			return result;
		if (spawned >= count)
			return result;

		int ticksSinceStart = currentTick - startTick;

		if (ticksSinceStart % interval == 0) {
			Enemy enemy = factory.create(path);
			result.add(enemy);
			spawned++;
		}

		return result;
	}

	/** Checks if the wave has finished spawning all its enemies. */
	public boolean isFinished() {
		return spawned >= count;
	}

	/** Get the unique identifier of the wave. */
	public int waveId() {
		return waveId;
	}
}
