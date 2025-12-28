package com.towerdefense.editor.core;

import java.util.ArrayList;
import java.util.List;

import com.towerdefense.editor.api.LevelEditor;
import com.towerdefense.editor.api.model.EditableAttack;
import com.towerdefense.editor.api.model.EditableLevel;
import com.towerdefense.editor.api.model.EditableMap;
import com.towerdefense.editor.api.model.EditablePath;
import com.towerdefense.editor.api.model.EditableTowerCapacity;
import com.towerdefense.editor.api.model.EditableWave;
import com.towerdefense.editor.api.model.LevelMetadata;
import com.towerdefense.editor.api.model.PathDefinition;
import com.towerdefense.editor.api.model.TowerCapacity;
import com.towerdefense.editor.api.model.TowerUpgrade;

public class DefaultLevelEditor implements LevelEditor {

    private EditableLevel level;
    private final EditableMap map;
    private final List<EditableAttack> attacks;
    private final List<EditableTowerCapacity> towerCapacities;

    public DefaultLevelEditor(LevelMetadata metadata, int mapWidth, int mapHeight) {
        this.map = new EditableMap(mapWidth, mapHeight);
        this.attacks = new ArrayList<>();
        this.towerCapacities = new ArrayList<>();

        this.level = new EditableLevel(
            metadata,
            map,
            attacks,
            towerCapacities
        );
    }

    @Override
    public void addPath(PathDefinition path) {
        map.addPath(new EditablePath(path.id(), path.points()));
    }

    @Override
    public void addTowerCapacity(TowerCapacity towerDefinition) {
        towerCapacities.add(
            new EditableTowerCapacity(towerDefinition.towerType())
        );
    }

    @Override
    public void addEnemyWave(int attackIndex, EditableWave wave) {
        ensureAttackExists(attackIndex);
        attacks.get(attackIndex).addWave(wave);
    }

    @Override
    public void upgradeTowerType(String towerType, TowerUpgrade upgrade) {
        EditableTowerCapacity capacity = towerCapacities.stream()
            .filter(c -> c.towerType().equals(towerType))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                "Unknown tower type: " + towerType
            ));

        capacity.addUpgrade(upgrade);
    }

    @Override
    public EditableLevel getCurrentLevel() {
        return level;
    }

    private void ensureAttackExists(int index) {
        while (attacks.size() <= index) {
            attacks.add(new EditableAttack());
        }
    }

	@Override
	public void loadLevel(EditableLevel level) {
		this.level = level;
	}
}
