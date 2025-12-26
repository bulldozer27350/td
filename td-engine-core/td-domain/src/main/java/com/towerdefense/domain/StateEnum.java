package com.towerdefense.domain;

/**
 * Represents the state of a game.
 * NOT_STARTED: The game has not started yet.
 * IN_PROGRESS: The game is currently in progress.
 * TERMINATED: The game has ended.
 */
public enum StateEnum {
	
	/** The game has not started yet. */
	NOT_STARTED, 
	/** The game is currently in progress. */
	IN_PROGRESS, 
	/** The game has ended. */
	TERMINATED
}
