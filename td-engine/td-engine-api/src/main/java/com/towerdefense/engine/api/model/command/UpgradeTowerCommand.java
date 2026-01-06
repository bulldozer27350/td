package com.towerdefense.engine.api.model.command;

import java.util.UUID;

public record UpgradeTowerCommand(int towerXPosition, int towerYPosition, UUID playerId) implements GameCommand {

}
