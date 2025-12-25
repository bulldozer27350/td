package com.towerdefense.domain.dynamik.tower;

import com.towerdefense.domain.player.PlayerState;

public interface TowerUpgradeService {

	boolean canUpgrade(Tower tower, PlayerState player);

	void upgrade(Tower tower, PlayerState player);

}