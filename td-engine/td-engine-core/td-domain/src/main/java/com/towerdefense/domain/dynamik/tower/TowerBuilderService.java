package com.towerdefense.domain.dynamik.tower;

import com.towerdefense.domain.GameState;
import com.towerdefense.domain.Position;
import com.towerdefense.domain.player.PlayerState;
import com.towerdefense.domain.statik.tower.TowerType;

/**
 * Service interface for building towers in the tower defense game.
 */
public interface TowerBuilderService {

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
	boolean canBuild(GameState state, PlayerState player, TowerType type, Position position);

	
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
	Tower build(GameState state, PlayerState player, TowerType type, Position position);

}