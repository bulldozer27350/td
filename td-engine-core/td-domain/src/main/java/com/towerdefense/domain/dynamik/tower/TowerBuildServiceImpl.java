package com.towerdefense.domain.dynamik.tower;

import org.springframework.stereotype.Service;

import com.towerdefense.domain.EntityId;
import com.towerdefense.domain.GameState;
import com.towerdefense.domain.Position;
import com.towerdefense.domain.player.PlayerState;
import com.towerdefense.domain.statik.tower.TowerLevelDefinition;
import com.towerdefense.domain.statik.tower.TowerType;

@Service
public class TowerBuildServiceImpl implements TowerBuilderService {

	@Override
	public boolean canBuild(GameState state,
            PlayerState player,
            TowerType type,
            Position position) {
		if (isPositionOccupied(state, position)) {
            return false;
        }

        TowerLevelDefinition baseLevel = type.level(1);
        return player.gold() >= baseLevel.upgradeCost();
	}

	@Override
	public Tower build(
            GameState state,
            PlayerState player,
            TowerType type,
            Position position
    ) {
        if (!canBuild(state, player, type, position)) {
            throw new IllegalStateException("Cannot build tower here");
        }

        TowerLevelDefinition baseLevel = type.level(1);

        player.spendGold(baseLevel.upgradeCost());

        return new Tower(EntityId.random(), position, type);
    }

    private boolean isPositionOccupied(GameState state, Position position) {
        return state.towers().stream()
                .anyMatch(t -> t.position().equals(position));
    }
}
