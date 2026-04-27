package com.towerdefense.services;

import java.util.List;

import com.towerdefense.config.EngineContext;
import com.towerdefense.domain.GameState;
import com.towerdefense.engine.api.GameStateObserver;
import com.towerdefense.engine.api.model.command.GameCommand;

public interface GameCommandHandler<C extends GameCommand> {
	Class<C> commandType();

    void handle(C command, GameState state, EngineContext context, List<GameStateObserver> observers, int tick);
}
