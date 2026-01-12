package com.towerdefense.editor.core.importer;

import com.towerdefense.editor.api.model.draft.EditableTowerLevel;
import com.towerdefense.editor.api.model.draft.EditableTowerType;
import com.towerdefense.engine.api.model.configuration.TowerLevelConfig;
import com.towerdefense.engine.api.model.configuration.TowerTypeConfig;

/**
 * Rebuilds an EditableLevel from an engine LevelConfig.
 * 
 * This class performs no validation.
 */
public class TowerTypeImportService {

	public EditableTowerType importLevel(TowerTypeConfig config) {
		EditableTowerType editableTowerType = new EditableTowerType(config.getId(), config.getId());
		for (TowerLevelConfig towerLevelConfig : config.getLevels()) {
			editableTowerType.upgrades().add(importTowerLevel(towerLevelConfig));
		}
		return editableTowerType;
	}

	private EditableTowerLevel importTowerLevel(TowerLevelConfig towerLevelConfig) {
		return new EditableTowerLevel(towerLevelConfig.getLevel(),
				towerLevelConfig.getUpgradeCost(), towerLevelConfig.getRange(), towerLevelConfig.getDamage(),
				towerLevelConfig.getReloadSeconds(), towerLevelConfig.getSellValue(), towerLevelConfig.getBuildTimeTicks());
	}

}
