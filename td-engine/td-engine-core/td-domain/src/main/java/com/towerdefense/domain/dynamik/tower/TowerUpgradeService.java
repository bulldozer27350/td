package com.towerdefense.domain.dynamik.tower;

import com.towerdefense.domain.GameState;
import com.towerdefense.domain.player.PlayerState;

/**
 * Service interface for upgrading towers in the tower defense game.
 */
public interface TowerUpgradeService {
	/**
	 * Checks if a tower can be upgraded by the player.
	 *
	 * @param tower  The tower to be upgraded.
	 * @param player The current state of the player.
	 * @param state  The current GameState.
	 * @return true if the tower can be upgraded, false otherwise.
	 */
	boolean canUpgrade(Tower tower, PlayerState player, GameState state);
	/**
	 * Upgrades the specified tower and updates the player's state accordingly.
	 *
	 * @param tower  The tower to be upgraded.
	 * @param player The current state of the player.
	 * @param state  The current game state.
	 */
	void upgrade(Tower tower, PlayerState player, GameState state);

}