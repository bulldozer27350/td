package com.towerdefense.editor.core.exporter;

import com.towerdefense.editor.api.model.draft.EditableTowerRank;
import com.towerdefense.editor.api.model.draft.EditableTowerType;
import com.towerdefense.editor.core.validation.TowerTypeValidator;
import com.towerdefense.editor.core.validation.ValidationResult;
import com.towerdefense.engine.api.model.configuration.TowerRankConfig;
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
	    for (EditableTowerRank editableTowerLevel : editableTower.ranks()) {
	        towerTypeConfig.getRanks().add(exportTowerType(editableTowerLevel));
	    }
	    return towerTypeConfig;
	}

	private TowerRankConfig exportTowerType(EditableTowerRank editableTowerRank) {
		return new TowerRankConfig(editableTowerRank.rank(), editableTowerRank.cost(),
				editableTowerRank.sellReward(), editableTowerRank.range(), editableTowerRank.damage(),
				editableTowerRank.reloadTime(), editableTowerRank.buildTimeTicks());
	}

}
