package com.towerdefense.editor.core.importer;

import java.util.ArrayList;
import java.util.List;

import com.towerdefense.editor.api.model.draft.EditableAttack;
import com.towerdefense.editor.api.model.draft.EditableLevel;
import com.towerdefense.editor.api.model.draft.EditableMap;
import com.towerdefense.editor.api.model.draft.EditableWave;
import com.towerdefense.editor.api.model.draft.TowerCapacity;
import com.towerdefense.engine.api.model.configuration.AttackConfig;
import com.towerdefense.engine.api.model.configuration.LevelConfig;
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
		EditableMap map = new EditableMap(10, 10);

		EditableLevel level = new EditableLevel(config.getId(), config.getStartingMoney(), config.getStartingLives(), map, new ArrayList<EditableAttack>(),
				new ArrayList<TowerCapacity>());

		// --- Paths ---
		// LevelConfig does not expose paths yet
		// They must be injected later or resolved externally

		// --- Attacks & Waves ---
		importAttacks(config.getAttacks(), level);

		// --- Tower capacities ---
		// Not present in LevelConfig yet

		return level;
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
}
