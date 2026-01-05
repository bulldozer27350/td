package com.towerdefense.engine.api.model.command;

import java.util.UUID;

public record PlaceTowerCommand(int x, int y, String towerType, UUID playerId) implements GameCommand {

}
