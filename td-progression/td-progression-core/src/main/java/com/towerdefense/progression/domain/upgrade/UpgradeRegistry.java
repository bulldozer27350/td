package com.towerdefense.progression.domain.upgrade;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.towerdefense.progression.model.UpgradeDefinitions;

//Registre de tous les upgrades disponibles
public class UpgradeRegistry {
	private final Map<String, TowerUpgrade> upgrades = new HashMap<>();
	private final Path upgradesJsonPath;

	public UpgradeRegistry(Path upgradesJsonPath) throws IOException {
		this.upgradesJsonPath = upgradesJsonPath;
		loadUpgrades(upgradesJsonPath);
	}

	private void loadUpgrades(Path jsonPath) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		upgrades.clear(); // Vider les upgrades existants
		File file = jsonPath.toFile();
		
		if (file != null && file.exists()) {
			UpgradeDefinitions definitions = mapper.readValue(file, UpgradeDefinitions.class);

			for (var def : definitions.upgrades()) {
				ModifierType modifierType = ModifierType.valueOf(def.effect().type());
				UpgradeEffect effect = new UpgradeEffect(def.effect().stat(), def.effect().modifier(), modifierType);

				TowerUpgrade upgrade = new TowerUpgrade(def.id(), def.name(), def.towerTypeId(), def.towerLevel(),
						def.cost(), effect);

				upgrades.put(upgrade.getId(), upgrade);
			}
		}

		System.out.println("[REGISTRY] Loaded " + upgrades.size() + " upgrades from " + jsonPath);
	}

	/**
	 * Recharge les upgrades depuis le fichier JSON
	 */
	public void reload() throws IOException {
		loadUpgrades(upgradesJsonPath);
	}

	public TowerUpgrade getUpgrade(String upgradeId) {
		return upgrades.get(upgradeId);
	}

	public List<TowerUpgrade> getAvailableUpgrades(String towerType, int level, int playerPoints) {
		return upgrades.values().stream().filter(u -> u.appliesTo(towerType, level))
				.filter(u -> u.getCost() <= playerPoints).toList();
	}

	public List<TowerUpgrade> getAllUpgrades() {
		return new ArrayList<>(upgrades.values());
	}

	public List<TowerUpgrade> getUpgradesForTower(String towerType) {
		return upgrades.values().stream().filter(u -> u.getTowerTypeId().equals(towerType)).toList();
	}
}
