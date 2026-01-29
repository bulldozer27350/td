package com.towerdefense.progression.modifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.towerdefense.progression.domain.upgrade.TowerUpgrade;
import com.towerdefense.progression.domain.upgrade.UpgradeEffect;
import com.towerdefense.progression.domain.upgrade.UpgradeRegistry;
import com.towerdefense.progression.model.TowerRank;
import com.towerdefense.progression.model.TowerTypeData;

public class TowerDataModifier {
	private final UpgradeRegistry upgradeRegistry;

	public TowerDataModifier(UpgradeRegistry upgradeRegistry) {
		this.upgradeRegistry = upgradeRegistry;
	}

	// Applique les upgrades débloqués sur les données de tour
	public TowerTypeData applyUpgrades(TowerTypeData originalTowerType, Set<String> unlockedUpgradeIds) {
		List<TowerRank> modifiedRanks = new ArrayList<>();

		for (TowerRank rank : originalTowerType.ranks()) {
			TowerRank modified = applyUpgradesToRank(originalTowerType.id(), rank, unlockedUpgradeIds);
			modifiedRanks.add(modified);
		}

		return new TowerTypeData(originalTowerType.id(), originalTowerType.name(), modifiedRanks);
	}

	private TowerRank applyUpgradesToRank(String towerTypeId, TowerRank towerRank, Set<String> unlockedUpgradeIds) {

		// Récupérer tous les upgrades applicables à ce rang de tour
		List<TowerUpgrade> applicableUpgrades = unlockedUpgradeIds.stream()
				// Transformation des TowerUpgrade correspondant à l'identifiant fourni
				.map(upgradeRegistry::getUpgrade)
				//
				.filter(Objects::nonNull)
				// qui puisse s'appliquer au bon type de tour et au bon niveau
				.filter(upgrade -> upgrade.appliesTo(towerTypeId, towerRank.rank()))
				//
				.toList();

		// Appliquer chaque upgrade
		double damage = towerRank.damage();
		double range = towerRank.range();
		double reloadSeconds = towerRank.reloadSeconds();

		for (TowerUpgrade upgrade : applicableUpgrades) {
			UpgradeEffect effect = upgrade.getEffect();

			switch (effect.stat()) {
			case "damage" -> damage = effect.apply(damage);
			case "range" -> range = effect.apply(range);
			case "reloadSeconds" -> reloadSeconds = effect.apply(reloadSeconds);
			}
		}

		return new TowerRank(towerRank.rank(), towerRank.upgradeCost(), towerRank.sellValue(), range, (int) Math.round(damage),
				reloadSeconds, towerRank.buildTimeTicks());
	}
}
