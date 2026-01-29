package com.towerdefense.services.internal.impl.command;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.towerdefense.domain.EntityId;
import com.towerdefense.domain.GameObject;
import com.towerdefense.domain.GameState;
import com.towerdefense.domain.Position;
import com.towerdefense.domain.dynamik.tower.Tower;
import com.towerdefense.domain.intentions.UpgradeTowerIntention;
import com.towerdefense.engine.api.GameStateObserver;
import com.towerdefense.engine.api.model.command.UpgradeTowerCommand;
import com.towerdefense.engine.api.model.events.TowerUpgradedEvent;
import com.towerdefense.services.GameCommandHandler;
import com.towerdefense.services.TowerServices;

@Component
public class UpgradeTowerCommandHandler implements GameCommandHandler<UpgradeTowerCommand> {

	private final TowerServices towerServices;

	public UpgradeTowerCommandHandler(TowerServices towerServices) {
		this.towerServices = towerServices;
	}

	@Override
	public void handle(UpgradeTowerCommand cmd, GameState state, List<GameStateObserver> observers, int tick) {
		GameObject tower = state.objectAt(new Position(cmd.towerXPosition(), cmd.towerYPosition()));
		if (tower instanceof Tower t) {
			UpgradeTowerIntention intention = new UpgradeTowerIntention(new EntityId(cmd.playerId()), t.id());
			Optional<Tower> oTower = towerServices.attemptUpgradeTower(state, intention);
			if (oTower.isPresent()) {
			    Tower upgradedTower = oTower.get();
			    upgradedTower.currentStats().rank();
			    System.out.println("[Tick " + tick + "]Sending event : Tower " + t.id().value() + " upgraded to rank "
			              + upgradedTower.currentStats().rank());
                observers.forEach(observer -> observer.onTowerUpgraded(new TowerUpgradedEvent(
                        t.id().value(), 
                        upgradedTower.currentStats().rank(), 
                        tick)));
            }
		} else {
			throw new IllegalArgumentException(
					"Tower has not been found in postition[" + cmd.towerXPosition() + ";" + cmd.towerYPosition() + "]");
		}
	}

	@Override
	public Class<UpgradeTowerCommand> commandType() {
		return UpgradeTowerCommand.class;
	}

}
