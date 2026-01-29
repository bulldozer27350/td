package com.towerdefense.editor.core.importer;

import java.util.ArrayList;
import java.util.List;

import com.towerdefense.editor.api.model.draft.EditableAttack;
import com.towerdefense.editor.api.model.draft.EditableLevel;
import com.towerdefense.editor.api.model.draft.EditableMap;
import com.towerdefense.editor.api.model.draft.EditablePath;
import com.towerdefense.editor.api.model.draft.EditableWave;
import com.towerdefense.editor.api.model.draft.TowerCapacity;
import com.towerdefense.editor.api.model.exportable.PositionDefinition;
import com.towerdefense.engine.api.model.configuration.AttackConfig;
import com.towerdefense.engine.api.model.configuration.LevelConfig;
import com.towerdefense.engine.api.model.configuration.PathConfig;
import com.towerdefense.engine.api.model.configuration.TowerCapacityConfig;
import com.towerdefense.engine.api.model.configuration.WaveConfig;

/**
 * Rebuilds an EditableLevel from an engine LevelConfig.
 * 
 * This class performs no validation.
 */
public class LevelImportService {

	public EditableLevel importLevel(LevelConfig config) {

		// --- Map ---
		// Dimensions are not part of LevelConfig yet → default placeholder
		EditableMap map = new EditableMap(config.getMap().getWidth(), config.getMap().getHeight());

		EditableLevel level = new EditableLevel(config.getId(), config.getStartingMoney(), config.getStartingLives(), map, new ArrayList<EditableAttack>(),
				new ArrayList<TowerCapacity>());

		// --- Paths ---
        importPaths(config.getPaths(), level.getPaths());

		// --- Attacks & Waves ---
		importAttacks(config.getAttacks(), level);

		// --- Tower capacities ---
		importTowerCapacities(config, level);

		return level;
	}

	private void importTowerCapacities(LevelConfig config, EditableLevel level) {
        for (TowerCapacityConfig towerCapacityConfig : config.getTowerCapacities()) {
            TowerCapacity towerCapacity = new TowerCapacity(towerCapacityConfig.getTowerTypeId(), towerCapacityConfig.getMaxRank());
            level.getTowerCapacities().add(towerCapacity);
        }
    }

    private void importAttacks(List<AttackConfig> attackConfigs, EditableLevel level) {
		for (int attackIndex = 0; attackIndex < attackConfigs.size(); attackIndex++) {
			AttackConfig attackConfig = attackConfigs.get(attackIndex);
			EditableAttack attack = new EditableAttack(attackConfig.getId());
			for (WaveConfig waveConfig : attackConfig.getWaves()) {
				EditableWave wave = new EditableWave(waveConfig.getId(), waveConfig.getStartTick(), waveConfig.getSpawnInterval(),
						waveConfig.getCount(), waveConfig.getEnemyType(), waveConfig.getPathId());
				attack.addWave(wave);
			}
			level.getAttacks().add(attack);
		}
	}
	
	private void importPaths(List<PathConfig> from, List<EditablePath> to) {
        for (PathConfig pathConfig : from) {
            EditablePath path = new EditablePath(pathConfig.getId(), pathConfig.getPoints().stream()
                    .map(point -> new PositionDefinition(point.getX(), point.getY()))
                    .toList());
            to.add(path);
        }
        
    }
}
