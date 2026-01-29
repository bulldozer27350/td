package com.towerdefense.domain.dynamik.tower;

import org.springframework.stereotype.Service;

import com.towerdefense.domain.EntityId;
import com.towerdefense.domain.GameState;
import com.towerdefense.domain.Position;
import com.towerdefense.domain.player.PlayerState;
import com.towerdefense.domain.statik.tower.TowerRankDefinition;
import com.towerdefense.domain.statik.tower.TowerType;

@Service
/**
 * Implementation of the TowerBuilderService interface.
 */
public class TowerBuildServiceImpl implements TowerBuilderService {

	@Override
	/**
	 * Checks if a tower of the specified type can be built at the given position
	 * by the player in the current game state.
	 *
	 * @param state    The current game state.
	 * @param player   The player attempting to build the tower.
	 * @param type     The type of tower to be built.
	 * @param position The position where the tower is to be built.
	 * @return true if the tower can be built, false otherwise.
	 */
	public boolean canBuild(GameState state, PlayerState player, TowerType type, Position position) {
		if (isPositionOccupied(state, position)) {
			return false;
		}

		TowerRankDefinition baseLevel = type.rank(1);
		return player.gold() >= baseLevel.upgradeCost();
	}

	@Override
	/**
	 * Builds a tower of the specified type at the given position for the player
	 * in the current game state.
	 *
	 * @param state    The current game state.
	 * @param player   The player building the tower.
	 * @param type     The type of tower to be built.
	 * @param position The position where the tower is to be built.
	 * @return The newly built Tower instance.
	 */
	public Tower build(GameState state, PlayerState player, TowerType type, Position position) {
		if (!canBuild(state, player, type, position)) {
			throw new IllegalStateException("Cannot build tower here");
		}

		TowerRankDefinition baseLevel = type.rank(1);

		player.spendGold(baseLevel.upgradeCost());

		return new Tower(EntityId.random(), position, type);
	}

	/**
	 * Checks if the specified position is already occupied by another tower.
	 *
	 * @param state    The current game state.
	 * @param position The position to check.
	 * @return true if the position is occupied, false otherwise.
	 */
	private boolean isPositionOccupied(GameState state, Position position) {
		return state.towers().stream().anyMatch(t -> t.position().equals(position));
	}
}
