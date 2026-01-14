package com.towerdefense.domain.dynamik.tower;

import org.springframework.stereotype.Service;

import com.towerdefense.domain.GameState;
import com.towerdefense.domain.player.PlayerState;
import com.towerdefense.domain.statik.tower.TowerLevelDefinition;

@Service
/**
 * Implementation of the TowerUpgradeService interface that provides
 * functionality to check if a tower can be upgraded and to perform the upgrade.
 */
public class TowerUpgradeServiceImpl implements TowerUpgradeService {

	/**
	 * Checks if the specified tower can be upgraded by the player.
	 *
	 * @param tower  The tower to check for upgrade eligibility.
	 * @param player The player's state, including available resources.
	 * @return true if the tower can be upgraded; false otherwise.
	 */
	@Override
	public boolean canUpgrade(Tower tower, PlayerState player, GameState state) {
	    if (!tower.canUpgrade())
	        return false;

	    int maxLevelAllowed = state.getTowerMaxLevel(tower.type().name());
	    if (tower.level() >= maxLevelAllowed) {
	        return false;
	    }

	    TowerLevelDefinition next = tower.nextLevelDefinition();
	    return player.gold() >= next.upgradeCost();
	}

	@Override
	/**
	 * Upgrades the specified tower for the player if the upgrade is allowed.
	 *
	 * @param tower  The tower to be upgraded.
	 * @param player The player's state, including available resources.
	 * @throws IllegalStateException if the upgrade is not allowed.
	 */
	public void upgrade(Tower tower, PlayerState player, GameState state) {
		if (!canUpgrade(tower, player, state)) {
			throw new IllegalStateException("Upgrade not allowed");
		}

		TowerLevelDefinition next = tower.nextLevelDefinition();

		player.spendGold(next.upgradeCost());
		tower.startUpgrade(next);
	}
}
