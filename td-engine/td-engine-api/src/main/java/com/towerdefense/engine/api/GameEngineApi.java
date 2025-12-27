package com.towerdefense.engine.api;

import com.towerdefense.engine.api.model.configuration.GameConfig;

public interface GameEngineApi {

	void startLevel(GameConfig gameConfig);
	
	void addObserver(GameStateObserver observer);
}
