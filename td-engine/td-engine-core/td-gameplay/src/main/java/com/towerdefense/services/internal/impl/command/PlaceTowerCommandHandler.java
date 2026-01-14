package com.towerdefense.services.internal.impl.command;

import org.springframework.stereotype.Component;

import com.towerdefense.config.EngineContext;
import com.towerdefense.domain.EntityId;
import com.towerdefense.domain.GameState;
import com.towerdefense.domain.Position;
import com.towerdefense.domain.intentions.BuildTowerIntention;
import com.towerdefense.domain.statik.level.LevelScenarioDefinition;
import com.towerdefense.domain.statik.tower.TowerType;
import com.towerdefense.engine.api.model.command.PlaceTowerCommand;
import com.towerdefense.exception.UnknownTowerTypeException;
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
	    // 1. Vérifier que le type de tour existe
	    TowerType towerType;
	    try {
	        towerType = this.context.towerTypeRegistry().get(cmd.towerType());
	    } catch (UnknownTowerTypeException e) {
	        throw e;
	    }
	    
	    // 2. ✅ NOUVEAU : Vérifier que la tour est autorisée sur ce niveau
	    if (!isTowerAllowedOnLevel(cmd.towerType())) {
	        throw new IllegalArgumentException(
	            "Tower type " + cmd.towerType() + " is not allowed on this level"
	        );
	    }
	    
	    BuildTowerIntention intention = new BuildTowerIntention(
	        new EntityId(cmd.playerId()), 
	        new Position(cmd.x(), cmd.y()), 
	        towerType
	    );
	    towerServices.attemptBuildTower(state, intention);
	}

	// ✅ NOUVELLE MÉTHODE
	private boolean isTowerAllowedOnLevel(String towerTypeId) {
	    LevelScenarioDefinition level = context.levelScenarioDefinition();
	    if (level == null || level.getTowerCapacities() == null) {
	        return true; // Rétrocompatibilité : si non spécifié, tout est autorisé
	    }
	    
	    return level.getTowerCapacities().stream()
	        .anyMatch(tc -> tc.getTowerTypeId().equalsIgnoreCase(towerTypeId));
	}
	
	@Override
    public Class<PlaceTowerCommand> commandType() {
        return PlaceTowerCommand.class;
    }

}
