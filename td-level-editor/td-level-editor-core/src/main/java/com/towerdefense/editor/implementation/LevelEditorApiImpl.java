package com.towerdefense.editor.implementation;

import java.util.ArrayList;
import java.util.List;

import com.towerdefense.editor.api.level.AttackEditorApi;
import com.towerdefense.editor.api.level.LevelEditorApi;
import com.towerdefense.editor.api.model.draft.EditableAttack;
import com.towerdefense.editor.api.model.draft.EditableLevel;
import com.towerdefense.editor.api.model.draft.EditablePath;

public class LevelEditorApiImpl implements LevelEditorApi {

	private final EditableLevel currentLevel;

	public LevelEditorApiImpl(EditableLevel editableLevel) {
		this.currentLevel = editableLevel;
	}

	@Override
	public EditableLevel getCurrentLevel() {
		return this.currentLevel;
	}

	@Override
	public boolean addPath(EditablePath path) {
		return currentLevel.getPaths().add(path);
	}

	@Override
	public boolean removePath(String pathId) {
		return currentLevel.getPaths().removeIf(e -> e.id().equalsIgnoreCase(pathId));
	}

	@Override
	public boolean addAttack(EditableAttack attack) {
		return currentLevel.getAttacks().add(attack);
	}

	@Override
	public AttackEditorApi getAttackEditor(String attackId) {
		EditableAttack editableAttack = this.currentLevel.getAttacks().stream().filter(e->e.getId().equalsIgnoreCase(attackId)).findFirst().orElse(null);
		if (editableAttack != null) {
			return new AttackEditorApiImpl(editableAttack);
		}
		throw new IllegalArgumentException("attackId has not been found.");
		
	}

	@Override
	public boolean removeAttack(String attackId) {
		return this.currentLevel.getAttacks().removeIf(e->e.getId().equalsIgnoreCase(attackId));
	}

	@Override
	public boolean addTowerType(String towerTypeId) {
		return currentLevel.getTowerCapacityIds().add(towerTypeId);
	}

	@Override
	public boolean removeTowerType(String towerTypeId) {
		return currentLevel.getTowerCapacityIds().removeIf(e->e.equalsIgnoreCase(towerTypeId));
	}

	@Override
	public List<AttackEditorApi> getAllAttackEditors() {
		List<AttackEditorApi> attackEditors = new ArrayList<>();
		for (EditableAttack attack : this.currentLevel.getAttacks()) {
			attackEditors.add(new AttackEditorApiImpl(attack));
		}
		return attackEditors;
	}

	@Override
	public boolean attributeInitialMoney(int initialMoney) {
		this.currentLevel.getMetadata().setStartingMoney(initialMoney);
		return true;
	}

	@Override
	public boolean attributeInitialLives(int initialLives) {
		this.currentLevel.getMetadata().setStartingLives(initialLives);
		return true;
	}

}
