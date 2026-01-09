package com.towerdefense.engine.api;

import com.towerdefense.engine.api.model.GameStateDTO;
import com.towerdefense.engine.api.model.command.GameCommand;
import com.towerdefense.engine.api.model.configuration.GameConfig;

public interface GameEngineApi {

	void initialize(GameConfig gameConfig);
	
	void addObserver(GameStateObserver observer);
	
	void dispatch(GameCommand command);
    
	void tick();
    
	boolean isGameOver();
	
	GameStateDTO getState();
}
