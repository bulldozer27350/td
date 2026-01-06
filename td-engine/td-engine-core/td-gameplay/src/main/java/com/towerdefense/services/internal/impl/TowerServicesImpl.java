package com.towerdefense.services.internal.impl;

import org.springframework.stereotype.Service;

import com.towerdefense.domain.GameState;
import com.towerdefense.domain.dynamik.tower.Tower;
import com.towerdefense.domain.dynamik.tower.TowerBuilderService;
import com.towerdefense.domain.dynamik.tower.TowerSellService;
import com.towerdefense.domain.dynamik.tower.TowerUpgradeService;
import com.towerdefense.domain.intentions.BuildTowerIntention;
import com.towerdefense.domain.intentions.SellTowerIntention;
import com.towerdefense.domain.intentions.UpgradeTowerIntention;
import com.towerdefense.domain.player.PlayerState;
import com.towerdefense.services.TowerServices;

@Service
/**
 * Implementation of tower-related services including building, upgrading, and
 * selling towers.
 */
public class TowerServicesImpl implements TowerServices {

	private final TowerUpgradeService upgradeService;

	private final TowerSellService sellService;

	private final TowerBuilderService buildService;

	/** Constructor to initialize tower services with required dependencies. */
	public TowerServicesImpl(TowerUpgradeService upgradeService, TowerSellService sellService,
			TowerBuilderService buildService) {
		this.upgradeService = upgradeService;
		this.sellService = sellService;
		this.buildService = buildService;
	}

	@Override
	/** Attempt to sell a tower based on the provided intention. */
	public boolean attemptSellTower(GameState state, SellTowerIntention intent) {
		Tower tower = state.towers().stream().filter(t -> t.id().equals(intent.towerId())).findFirst().orElseThrow();
		PlayerState player = state.player();

		if (!this.sellService.canSell(tower, player)) {
			return false;
		}

		this.sellService.sell(tower, player);
		state.removeTower(tower.id());
		return true;
	}

	@Override
	/** Attempt to upgrade a tower based on the provided intention. */
	public boolean attemptUpgradeTower(GameState state, UpgradeTowerIntention intent) {
		Tower tower = state.towers().stream().filter(t -> t.id().equals(intent.towerId())).findFirst().orElseThrow();

		PlayerState player = state.player();

		if (!this.upgradeService.canUpgrade(tower, player)) {
			return false;
		}
		this.upgradeService.upgrade(tower, player);
		return true;
	}

	@Override
	/** Attempt to build a tower based on the provided intention. */
	public boolean attemptBuildTower(GameState state, BuildTowerIntention intent) {
		if (!state.player().id().equals(intent.playerId())) {
			return false;
		}
		if (!this.buildService.canBuild(state, state.player(), intent.towerType(), intent.position())) {
			return false;
		}

		Tower tower = this.buildService.build(state, state.player(), intent.towerType(), intent.position());
		state.addTower(tower);
		return true;
	}

}
