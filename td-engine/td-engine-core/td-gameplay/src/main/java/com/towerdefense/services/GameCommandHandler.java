package com.towerdefense.services;

import com.towerdefense.domain.GameState;
import com.towerdefense.engine.api.model.command.GameCommand;

public interface GameCommandHandler<C extends GameCommand> {
	Class<C> commandType();

    void handle(C command, GameState state);
}
