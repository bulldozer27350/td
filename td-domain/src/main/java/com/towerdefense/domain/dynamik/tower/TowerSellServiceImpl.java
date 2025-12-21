package com.towerdefense.domain.dynamik.tower;

import org.springframework.stereotype.Service;

import com.towerdefense.domain.player.PlayerState;
import com.towerdefense.domain.statik.tower.TowerLevelDefinition;

@Service
public class TowerSellServiceImpl implements TowerSellService {

	@Override
	public boolean canSell(Tower tower, PlayerState player) {
		// Mise à part si la tour est en cours de construction, aucune raison de refuser
		// la vente.
		return !tower.isUnderBuilding();
	}

	@Override
	public void sell(Tower tower, PlayerState player) {
		if (!canSell(tower, player)) {
			throw new IllegalStateException("Sell not allowed");
		}

		TowerLevelDefinition current = tower.currentStats();

		player.earnGold(current.sellValue());
	}
}
