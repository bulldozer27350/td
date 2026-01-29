package com.towerdefense.editor.core.exporter;

import java.util.List;
import java.util.stream.Collectors;

import com.towerdefense.editor.api.model.draft.EditableAttack;
import com.towerdefense.editor.api.model.draft.EditableLevel;
import com.towerdefense.editor.api.model.draft.EditablePath;
import com.towerdefense.editor.core.validation.LevelValidator;
import com.towerdefense.editor.core.validation.ValidationResult;
import com.towerdefense.engine.api.model.configuration.AttackConfig;
import com.towerdefense.engine.api.model.configuration.LevelConfig;
import com.towerdefense.engine.api.model.configuration.MapDimensions;
import com.towerdefense.engine.api.model.configuration.PathConfig;
import com.towerdefense.engine.api.model.configuration.PointConfig;
import com.towerdefense.engine.api.model.configuration.TowerCapacityConfig;
import com.towerdefense.engine.api.model.configuration.WaveConfig;

public class LevelExportService {

	private final LevelValidator validator;

	public LevelExportService(LevelValidator validator) {
		this.validator = validator;
	}

	public LevelConfig export(EditableLevel level) {
		ValidationResult validation = validator.validate(level);
		if (!validation.isValid()) {
			throw new IllegalStateException("Invalid rank: " + validation.getErrors());
		}
		LevelConfig levelConfig = new LevelConfig();
		List<AttackConfig> attacks = level.getAttacks().stream().map(this::mapAttack).toList();
		
		List<TowerCapacityConfig> towerCapacities = level.getTowerCapacities().stream()
			    .map(tc -> new TowerCapacityConfig(tc.getTowerTypeId(), tc.getMaxLevel()))
			    .collect(Collectors.toList());
		
		List<PathConfig> paths = level.getPaths().stream().map(this::mapPath).toList();
		
		levelConfig.setId(level.getId());
		levelConfig.setAttacks(attacks);
		levelConfig.setStartingLives(level.getStartingLives());
		levelConfig.setStartingMoney(level.getStartingMoney());
		levelConfig.setTowerCapacities(towerCapacities);
		levelConfig.setMap(new MapDimensions(level.getMap().getWidth(), level.getMap().getHeight()));
		levelConfig.setPaths(paths);
		return levelConfig;
	}

	private AttackConfig mapAttack(EditableAttack attack) {
		AttackConfig attackConfig = new AttackConfig(attack.getId(),
				attack.getWaves().stream().map(wave -> new WaveConfig(wave.getId(), wave.getStartTick(), wave.getSpawnInterval(),
						wave.getCount(), wave.getEnemyType(), wave.getPathId())).toList());
		return attackConfig;
	}
	
	private PathConfig mapPath(EditablePath path) {
	    PathConfig config = new PathConfig();
	    config.setId(path.id());
	    config.setPoints(path.points().stream().map(p -> 
	    {
	        PointConfig pc = new PointConfig();
	        pc.setX(p.x());
	        pc.setY(p.y());
	        return pc;
	    }).toList());
	    return config;
	}
}
