package com.towerdefense.services.internal.impl.command;

import org.springframework.stereotype.Component;

import com.towerdefense.domain.EntityId;
import com.towerdefense.domain.GameObject;
import com.towerdefense.domain.GameState;
import com.towerdefense.domain.Position;
import com.towerdefense.domain.dynamik.tower.Tower;
import com.towerdefense.domain.intentions.SellTowerIntention;
import com.towerdefense.engine.api.model.command.SellTowerCommand;
import com.towerdefense.services.GameCommandHandler;
import com.towerdefense.services.TowerServices;

@Component
public class SellTowerCommandHandler implements GameCommandHandler<SellTowerCommand> {

	private final TowerServices towerServices;

	public SellTowerCommandHandler(TowerServices towerServices) {
		this.towerServices = towerServices;
	}

	@Override
	public void handle(SellTowerCommand cmd, GameState state) {
		GameObject tower = state.objectAt(new Position(cmd.towerXPosition(), cmd.towerYPosition()));
		if (tower instanceof Tower t) {
			SellTowerIntention intention = new SellTowerIntention(new EntityId(cmd.playerId()), t.id());
			towerServices.attemptSellTower(state, intention);
		} else {
			throw new IllegalArgumentException(
					"Tower has not been found in postition[" + cmd.towerXPosition() + ";" + cmd.towerYPosition() + "]");
		}
	}

	@Override
	public Class<SellTowerCommand> commandType() {
		return SellTowerCommand.class;
	}

}
