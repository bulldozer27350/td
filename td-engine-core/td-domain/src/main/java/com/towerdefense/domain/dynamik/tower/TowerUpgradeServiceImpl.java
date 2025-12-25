package com.towerdefense.domain.dynamik.tower;

import org.springframework.stereotype.Service;

import com.towerdefense.domain.player.PlayerState;
import com.towerdefense.domain.statik.tower.TowerLevelDefinition;

@Service
public class TowerUpgradeServiceImpl implements TowerUpgradeService {

    @Override
	public boolean canUpgrade(Tower tower, PlayerState player) {
        if (!tower.canUpgrade()) return false;

        TowerLevelDefinition next = tower.nextLevelDefinition();
        return player.gold() >= next.upgradeCost();
    }

    @Override
	public void upgrade(Tower tower, PlayerState player) {
        if (!canUpgrade(tower, player)) {
            throw new IllegalStateException("Upgrade not allowed");
        }

        TowerLevelDefinition next = tower.nextLevelDefinition();

        player.spendGold(next.upgradeCost());
        tower.startUpgrade(next);
    }
}
