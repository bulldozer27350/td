package com.towerdefense.editor.core.exporter;

import java.util.List;
import java.util.stream.Collectors;

import com.towerdefense.editor.api.model.EditableAttack;
import com.towerdefense.editor.api.model.EditableLevel;
import com.towerdefense.editor.core.validation.LevelValidator;
import com.towerdefense.editor.core.validation.ValidationResult;
import com.towerdefense.engine.api.model.configuration.AttackConfig;
import com.towerdefense.engine.api.model.configuration.LevelConfig;
import com.towerdefense.engine.api.model.configuration.WaveConfig;

public class LevelExportService {

	private final LevelValidator validator;

	public LevelExportService(LevelValidator validator) {
		this.validator = validator;
	}

	public LevelConfig export(EditableLevel level) {
		ValidationResult validation = validator.validate(level);
		if (!validation.isValid()) {
			throw new IllegalStateException("Invalid level: " + validation.getErrors());
		}
		LevelConfig levelConfig = new LevelConfig();
		List<AttackConfig> attacks = level.getAttacks().stream().map(this::mapAttack).collect(Collectors.toList());
		levelConfig.setId(level.getMetadata().getLevelId());
		levelConfig.setAttacks(attacks);
		levelConfig.setStartingLives(level.getMetadata().getStartingLives());
		levelConfig.setStartingMoney(level.getMetadata().getStartingMoney());
		return levelConfig;
	}

	private AttackConfig mapAttack(EditableAttack attack) {
		AttackConfig attackConfig = new AttackConfig(
				attack.getWaves().stream().map(wave -> new WaveConfig(wave.getStartTick(), wave.getSpawnInterval(),
						wave.getCount(), wave.getEnemyType(), wave.getPathId())).collect(Collectors.toList()));
		return attackConfig;
	}
}
