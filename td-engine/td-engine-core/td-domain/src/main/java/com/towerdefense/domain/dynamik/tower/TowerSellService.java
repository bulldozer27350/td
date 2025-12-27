package com.towerdefense.domain.dynamik.tower;

import com.towerdefense.domain.player.PlayerState;

/**
 * Service interface for selling towers in the tower defense game.
 */
public interface TowerSellService {

	/**
	 * Checks if a tower can be sold by the player.
	 *
	 * @param tower  The tower to be sold.
	 * @param player The current state of the player.
	 * @return true if the tower can be sold, false otherwise.
	 */
	boolean canSell(Tower tower, PlayerState player);

	/**
	 * Sells the specified tower and updates the player's state accordingly.
	 *
	 * @param tower  The tower to be sold.
	 * @param player The current state of the player.
	 */
	void sell(Tower tower, PlayerState player);

}