package com.towerdefense.editor.implementation;

import java.util.ArrayList;
import java.util.List;

import com.towerdefense.editor.api.level.AttackEditorApi;
import com.towerdefense.editor.api.model.draft.EditableAttack;
import com.towerdefense.editor.api.model.draft.EditableWave;

public class AttackEditorApiImpl implements AttackEditorApi {

	private final EditableAttack current;
	
	public AttackEditorApiImpl(EditableAttack editableAttack) {
		current = editableAttack;
	}

	@Override
	public EditableAttack getCurrent() {
		return this.current;
	}

	@Override
	public List<EditableWave> getAllEditableWaves() {
		return new ArrayList<>(this.current.getWaves());
	}

	@Override
	public EditableWave getEditableWave(String id) {
		return this.current.getWaves().stream().filter(e->e.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
	}

	@Override
	public boolean addEditableWave(String id, int startTick, int spawnInterval, int count, String enemyType,
			String pathId) {
		return this.current.getWaves().add(new EditableWave(id, startTick, spawnInterval, count, enemyType, pathId));
	}

	@Override
	public boolean removeEditableWave(String id) {
		return this.current.getWaves().removeIf(e->e.getId().equalsIgnoreCase(id));
	}

}
