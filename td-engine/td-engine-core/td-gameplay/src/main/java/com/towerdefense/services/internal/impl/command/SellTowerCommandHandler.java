package com.towerdefense.services.internal.impl.command;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.towerdefense.config.EngineContext;
import com.towerdefense.domain.EntityId;
import com.towerdefense.domain.GameObject;
import com.towerdefense.domain.GameState;
import com.towerdefense.domain.Position;
import com.towerdefense.domain.dynamik.tower.Tower;
import com.towerdefense.domain.intentions.SellTowerIntention;
import com.towerdefense.engine.api.GameStateObserver;
import com.towerdefense.engine.api.model.command.SellTowerCommand;
import com.towerdefense.engine.api.model.events.TowerSoldEvent;
import com.towerdefense.services.GameCommandHandler;
import com.towerdefense.services.TowerServices;

@Component
public class SellTowerCommandHandler implements GameCommandHandler<SellTowerCommand> {

	private final TowerServices towerServices;

	public SellTowerCommandHandler(TowerServices towerServices) {
		this.towerServices = towerServices;
	}

	@Override
	public void handle(SellTowerCommand cmd, GameState state, EngineContext context, List<GameStateObserver> observers, int tick) {
		GameObject tower = state.objectAt(new Position(cmd.towerXPosition(), cmd.towerYPosition()));
		if (tower instanceof Tower t) {
			SellTowerIntention intention = new SellTowerIntention(new EntityId(cmd.playerId()), t.id());
			Optional<Tower> oTower = towerServices.attemptSellTower(state, intention);
			if (oTower.isPresent()) {
			    observers.forEach(observer -> {
                    Tower soldTower = oTower.get();
                    System.out.println("[Tick " + tick + "]Sending event : Tower " + soldTower.id().value() + " sold for "
                            + soldTower.currentStats().sellValue());
                    observer.onTowerSold(
                            new TowerSoldEvent(
                                    soldTower.id().value(),
                                    soldTower.currentStats().sellValue(),
                                    tick));
                });
            }
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
