package com.towerdefense.editor.core.importer;

import com.towerdefense.editor.api.model.draft.EditableTowerRank;
import com.towerdefense.editor.api.model.draft.EditableTowerType;
import com.towerdefense.engine.api.model.configuration.TowerRankConfig;
import com.towerdefense.engine.api.model.configuration.TowerTypeConfig;

/**
 * Rebuilds an EditableLevel from an engine LevelConfig.
 * 
 * This class performs no validation.
 */
public class TowerTypeImportService {

	public EditableTowerType importLevel(TowerTypeConfig config) {
		EditableTowerType editableTowerType = new EditableTowerType(config.getName(), config.getId());
		for (TowerRankConfig towerLevelConfig : config.getRanks()) {
			editableTowerType.ranks().add(importTowerLevel(towerLevelConfig));
		}
		return editableTowerType;
	}

	private EditableTowerRank importTowerLevel(TowerRankConfig towerLevelConfig) {
		return new EditableTowerRank(towerLevelConfig.getRank(),
				towerLevelConfig.getUpgradeCost(), towerLevelConfig.getRange(), towerLevelConfig.getDamage(),
				towerLevelConfig.getReloadSeconds(), towerLevelConfig.getSellValue(), towerLevelConfig.getBuildTimeTicks());
	}

}
