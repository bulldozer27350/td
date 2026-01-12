package com.towerdefense.editor.core.exporter;

import com.towerdefense.editor.api.model.draft.EditableTowerLevel;
import com.towerdefense.editor.api.model.draft.EditableTowerType;
import com.towerdefense.editor.core.validation.TowerTypeValidator;
import com.towerdefense.editor.core.validation.ValidationResult;
import com.towerdefense.engine.api.model.configuration.TowerLevelConfig;
import com.towerdefense.engine.api.model.configuration.TowerTypeConfig;

public class TowerTypeExportService {

	private final TowerTypeValidator validator;

	public TowerTypeExportService(TowerTypeValidator validator) {
		this.validator = validator;
	}

	public TowerTypeConfig export(EditableTowerType editableTower) {
	    ValidationResult validation = validator.validate(editableTower);
	    if (!validation.isValid()) {
	        throw new IllegalStateException("Invalid tower type: " + validation.getErrors());
	    }
	    TowerTypeConfig towerTypeConfig = new TowerTypeConfig();
	    towerTypeConfig.setId(editableTower.id());
	    towerTypeConfig.setName(editableTower.towerType()); // ✅ AJOUT DE CETTE LIGNE
	    for (EditableTowerLevel editableTowerLevel : editableTower.upgrades()) {
	        towerTypeConfig.getLevels().add(exportTowerType(editableTowerLevel));
	    }
	    return towerTypeConfig;
	}

	private TowerLevelConfig exportTowerType(EditableTowerLevel editableTowerLevel) {
		return new TowerLevelConfig(editableTowerLevel.level(), editableTowerLevel.cost(),
				editableTowerLevel.sellReward(), editableTowerLevel.range(), editableTowerLevel.damage(),
				editableTowerLevel.reloadTime(), editableTowerLevel.buildTimeTicks());
	}

}
