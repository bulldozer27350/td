package com.towerdefense.domain.dynamik.tower;

import org.springframework.stereotype.Service;

import com.towerdefense.domain.player.PlayerState;
import com.towerdefense.domain.statik.tower.TowerRankDefinition;

@Service
/**
 * Service gérant la vente des tours.
 */
public class TowerSellServiceImpl implements TowerSellService {

	@Override
	/**
	 * Vérifie si une tour peut être vendue par le joueur.
	 *
	 * @param tower  La tour à vendre.
	 * @param player L'état actuel du joueur.
	 * @return true si la tour peut être vendue, false sinon.
	 */
	public boolean canSell(Tower tower, PlayerState player) {
		// Mise à part si la tour est en cours de construction, aucune raison de refuser
		// la vente.
		return !tower.isUnderBuilding();
	}

	@Override
	/**
	 * Vend la tour spécifiée et met à jour l'état du joueur en conséquence.
	 *
	 * @param tower  La tour à vendre.
	 * @param player L'état actuel du joueur.
	 */
	public void sell(Tower tower, PlayerState player) {
		if (!canSell(tower, player)) {
			throw new IllegalStateException("Sell not allowed");
		}

		TowerRankDefinition current = tower.currentStats();

		player.earnGold(current.sellValue());
	}
}
