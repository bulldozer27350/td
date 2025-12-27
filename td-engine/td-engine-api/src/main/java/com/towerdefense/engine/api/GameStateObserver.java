package com.towerdefense.engine.api;

import com.towerdefense.engine.api.model.GameStateDTO;
import com.towerdefense.engine.api.model.LevelMapDTO;

public interface GameStateObserver {

	void onStateUpdated(GameStateDTO state, int tick);
	
	void onGameCreated(LevelMapDTO levelMap);

	void onGameWon(GameStateDTO state);

	void onGameLoose();

}
