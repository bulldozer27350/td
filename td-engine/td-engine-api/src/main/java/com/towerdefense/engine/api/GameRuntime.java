package com.towerdefense.engine.api;

import com.towerdefense.engine.api.model.GameStateDTO;
import com.towerdefense.engine.api.model.command.GameCommand;
import com.towerdefense.engine.api.model.configuration.GameConfig;

public interface GameRuntime {
	void initialize(GameConfig config);

    void submit(GameCommand command);

    void tick();

    GameStateDTO getState();

    boolean isGameOver();

    void addObserver(GameStateObserver observer);
}