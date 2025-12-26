package com.towerdefense.orchestrator.listener;

import com.towerdefense.domain.GameState;

/**
 * Observer interface for receiving updates about the game state.
 */
public interface GameStateObserver {

	/**
	 * Called when the game state is updated.
	 *
	 * @param state The updated game state.
	 * @param tick  The current tick of the game.
	 */
	void onStateUpdated(GameState state, int tick);

}
