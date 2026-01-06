package com.towerdefense.services.internal.impl.command;

import org.springframework.stereotype.Component;

import com.towerdefense.config.EngineContext;
import com.towerdefense.domain.EntityId;
import com.towerdefense.domain.GameState;
import com.towerdefense.domain.Position;
import com.towerdefense.domain.intentions.BuildTowerIntention;
import com.towerdefense.domain.statik.tower.TowerType;
import com.towerdefense.engine.api.model.command.PlaceTowerCommand;
import com.towerdefense.services.GameCommandHandler;
import com.towerdefense.services.TowerServices;

@Component
public class PlaceTowerCommandHandler implements GameCommandHandler<PlaceTowerCommand> {

	private final TowerServices towerServices;
	
	private final EngineContext context;

	public PlaceTowerCommandHandler(TowerServices towerServices, EngineContext engineContext) {
		this.towerServices = towerServices;
		this.context = engineContext;
	}
	
	@Override
	public void handle(PlaceTowerCommand cmd, GameState state) {
		TowerType towerType = this.context.towerTypeRegistry().get(cmd.towerType());
		BuildTowerIntention intention = new BuildTowerIntention(new EntityId(cmd.playerId()), new Position(cmd.x(), cmd.y()), towerType);
		towerServices.attemptBuildTower(
	            state,
	            intention
	        );
	}
	
	@Override
    public Class<PlaceTowerCommand> commandType() {
        return PlaceTowerCommand.class;
    }

}
