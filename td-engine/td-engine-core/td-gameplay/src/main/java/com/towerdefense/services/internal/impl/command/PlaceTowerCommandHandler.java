package com.towerdefense.services.internal.impl.command;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.towerdefense.config.EngineContext;
import com.towerdefense.domain.EntityId;
import com.towerdefense.domain.GameState;
import com.towerdefense.domain.Position;
import com.towerdefense.domain.dynamik.tower.Tower;
import com.towerdefense.domain.intentions.BuildTowerIntention;
import com.towerdefense.domain.projectile.Projectile;
import com.towerdefense.domain.statik.level.LevelScenarioDefinition;
import com.towerdefense.domain.statik.tower.TowerType;
import com.towerdefense.engine.api.GameStateObserver;
import com.towerdefense.engine.api.model.PositionDTO;
import com.towerdefense.engine.api.model.TowerDTO;
import com.towerdefense.engine.api.model.TowerStateEnum;
import com.towerdefense.engine.api.model.command.PlaceTowerCommand;
import com.towerdefense.engine.api.model.events.TowerPlacedEvent;
import com.towerdefense.exception.UnknownTowerTypeException;
import com.towerdefense.services.GameCommandHandler;
import com.towerdefense.services.TowerServices;

@Component
public class PlaceTowerCommandHandler implements GameCommandHandler<PlaceTowerCommand> {

	private final TowerServices towerServices;

	public PlaceTowerCommandHandler(TowerServices towerServices) {
		this.towerServices = towerServices;
	}
	
	@Override
	public void handle(PlaceTowerCommand cmd, GameState state, EngineContext context, List<GameStateObserver> observers, int tick) {
	    TowerType towerType;
	    if (!positionFree(state, cmd.x(), cmd.y())) {
            throw new IllegalArgumentException(
                "Cannot place tower at position (" + cmd.x() + ", " + cmd.y() + "): position is not free"
            );
        }
	    try {
	        towerType = context.towerTypeRegistry().get(cmd.towerType());
	    } catch (UnknownTowerTypeException e) {
	        throw e;
	    }
	    
	    if (!isTowerAllowedOnLevel(context, cmd.towerType())) {
	        throw new IllegalArgumentException(
	            "Tower type " + cmd.towerType() + " is not allowed on this level"
	        );
	    }
	    
	    BuildTowerIntention intention = new BuildTowerIntention(
	        new EntityId(cmd.playerId()), 
	        new Position(cmd.x(), cmd.y()), 
	        towerType
	    );
	    Optional<Tower> oTower = towerServices.attemptBuildTower(state, intention);
	    if (oTower.isPresent()) {
	        System.out.println("[Tick " + tick + "]Sending event : Tower " + oTower.get().id().value() + " placed at position (" 
                + oTower.get().position().x() + ", " + oTower.get().position().y() + ")");
	        observers.forEach(observer -> observer.onTowerPlaced(new TowerPlacedEvent(toDTO(oTower.get()), tick)));
	    }
	}

	private boolean positionFree(GameState state, int x, int y) {
	    return state.objectAt(new Position(x, y)) == null || state.objectAt(new Position(x, y)) instanceof Projectile;
    }

    private static TowerDTO toDTO(Tower tower) {
        String state;
        if (tower.isUnderBuilding()) {
            state = "BUILDING";
        } else if (!tower.isReady()) {
            state = "RELOADING";
        } else {
            state = "READY";
        }

        return new TowerDTO(tower.id().value().toString(), new PositionDTO(tower.position().x(), tower.position().y()),
                tower.type().name(), 
                TowerStateEnum.valueOf(state));
    }
	
	private boolean isTowerAllowedOnLevel(EngineContext context, String towerTypeId) {
	    LevelScenarioDefinition level = context.levelScenarioDefinition();
	    if (level == null || level.getTowerCapacities() == null) {
	        return true; 
	    }
	    
	    return level.getTowerCapacities().stream()
	        .anyMatch(tc -> tc.getTowerTypeId().equalsIgnoreCase(towerTypeId));
	}
	
	@Override
    public Class<PlaceTowerCommand> commandType() {
        return PlaceTowerCommand.class;
    }

}
