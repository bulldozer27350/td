package com.towerdefense.domain.dynamik.tower;

import com.towerdefense.domain.player.PlayerState;

public interface TowerSellService {

	boolean canSell(Tower tower, PlayerState player);

	void sell(Tower tower, PlayerState player);

}