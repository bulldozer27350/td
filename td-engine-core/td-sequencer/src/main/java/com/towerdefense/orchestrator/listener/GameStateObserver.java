package com.towerdefense.orchestrator.listener;

import com.towerdefense.domain.GameState;

public interface GameStateObserver {

	void onStateUpdated(GameState state, int tick);

}
