package com.towerdefense.editor.core.exporter;

import com.towerdefense.editor.api.model.draft.EditableEnemyType;
import com.towerdefense.editor.core.validation.EnemyTypeValidator;
import com.towerdefense.editor.core.validation.ValidationResult;
import com.towerdefense.engine.api.model.configuration.EnemyTypeConfig;

public class EnemyTypeExportService {

	private final EnemyTypeValidator validator;

	public EnemyTypeExportService(EnemyTypeValidator validator) {
		this.validator = validator;
	}

	public EnemyTypeConfig export(EditableEnemyType editableEnemyType) {
		ValidationResult validation = validator.validate(editableEnemyType);
		if (!validation.isValid()) {
			throw new IllegalStateException("Invalid enemi type: " + validation.getErrors());
		}
		EnemyTypeConfig enemyTypeConfig = new EnemyTypeConfig();
		enemyTypeConfig.setId(editableEnemyType.getId());
		enemyTypeConfig.setHp(editableEnemyType.getHealth());
		enemyTypeConfig.setBounty(editableEnemyType.getReward());
		enemyTypeConfig.setSpeed(editableEnemyType.getSpeed());
		return enemyTypeConfig;
	}

}
