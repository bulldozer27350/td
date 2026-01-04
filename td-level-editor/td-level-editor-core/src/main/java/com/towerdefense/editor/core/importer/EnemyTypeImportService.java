package com.towerdefense.editor.core.importer;

import com.towerdefense.editor.api.model.draft.EditableEnemyType;
import com.towerdefense.engine.api.model.configuration.EnemyTypeConfig;

/**
 * Rebuilds an EditableEnemyType from an engine EnemyTypeConfig.
 * 
 * This class performs no validation.
 */
public class EnemyTypeImportService {

	public EditableEnemyType importEnemyType(EnemyTypeConfig config) {
		return new EditableEnemyType(config.getId(), config.getHp(), config.getSpeed(), config.getBounty());
	}
}
