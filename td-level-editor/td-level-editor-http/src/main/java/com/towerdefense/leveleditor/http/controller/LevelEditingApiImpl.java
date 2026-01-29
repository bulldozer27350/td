package com.towerdefense.leveleditor.http.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.towerdefense.editor.api.level.AttackEditorApi;
import com.towerdefense.editor.api.level.LevelEditorApi;
import com.towerdefense.leveleditor.http.api.LevelEditingApi;
import com.towerdefense.leveleditor.http.mapper.level.EditableAttackMapper;
import com.towerdefense.leveleditor.http.mapper.level.EditableLevelMapper;
import com.towerdefense.leveleditor.http.mapper.level.EditablePathMapper;
import com.towerdefense.leveleditor.http.mapper.level.EditableWaveMapper;
import com.towerdefense.leveleditor.http.model.AddTowerTypeToLevelRequest;
import com.towerdefense.leveleditor.http.model.CreateAttackRequest;
import com.towerdefense.leveleditor.http.model.EditableAttack;
import com.towerdefense.leveleditor.http.model.EditableLevel;
import com.towerdefense.leveleditor.http.model.EditablePath;
import com.towerdefense.leveleditor.http.model.EditableWave;
import com.towerdefense.leveleditor.http.model.LevelMetadataUpdate;
import com.towerdefense.leveleditor.http.model.UpdateTowerCapacityRequest;

import jakarta.validation.Valid;

@RestController
public class LevelEditingApiImpl extends AbstractEditorController implements LevelEditingApi {

	private final EditablePathMapper pathMapper;
	private final EditableAttackMapper attackMapper;
	private final EditableWaveMapper waveMapper;
	private final EditableLevelMapper levelMapper;

	public LevelEditingApiImpl(EditablePathMapper pathMapper, EditableAttackMapper attackMapper,
			EditableWaveMapper waveMapper, EditableLevelMapper levelMapper) {
		this.pathMapper = pathMapper;
		this.attackMapper = attackMapper;
		this.waveMapper = waveMapper;
		this.levelMapper = levelMapper;
	}

	@Override
	public ResponseEntity<Void> addAttack(String levelId, @Valid CreateAttackRequest createAttackRequest) {
		LevelEditorApi levelEditor = getLevelEditor(levelId);
		if (levelEditor == null) {
			return ResponseEntity.notFound().build();
		}

		com.towerdefense.editor.api.model.draft.EditableAttack attack = new com.towerdefense.editor.api.model.draft.EditableAttack(
				createAttackRequest.getId());

		boolean success = levelEditor.addAttack(attack);

		return success ? ResponseEntity.status(HttpStatus.CREATED).build()
				: ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	@Override
	public ResponseEntity<Void> addPath(String levelId, @Valid EditablePath editablePath) {
		LevelEditorApi levelEditor = getLevelEditor(levelId);
		if (levelEditor == null) {
			return ResponseEntity.notFound().build();
		}

		com.towerdefense.editor.api.model.draft.EditablePath domainPath = pathMapper.toDomain(editablePath);
		boolean success = levelEditor.addPath(domainPath);

		return success ? ResponseEntity.status(HttpStatus.CREATED).build()
				: ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	@Override
	public ResponseEntity<Void> addWave(String levelId, String attackId, @Valid EditableWave editableWave) {
		LevelEditorApi levelEditor = getLevelEditor(levelId);
		if (levelEditor == null) {
			return ResponseEntity.notFound().build();
		}

		try {
			AttackEditorApi attackEditor = levelEditor.getAttackEditor(attackId);
			com.towerdefense.editor.api.model.draft.EditableWave domainWave = waveMapper.toDomain(editableWave);

			boolean success = attackEditor.addEditableWave(domainWave.getId(), domainWave.getStartTick(),
					domainWave.getSpawnInterval(), domainWave.getCount(), domainWave.getEnemyType(),
					domainWave.getPathId());

			return success ? ResponseEntity.status(HttpStatus.CREATED).build()
					: ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@Override
	public ResponseEntity<List<EditableAttack>> getLevelAttacks(String levelId) {
		LevelEditorApi levelEditor = getLevelEditor(levelId);
		if (levelEditor == null) {
			return ResponseEntity.notFound().build();
		}

		List<EditableAttack> attacks = levelEditor.getAllAttackEditors().stream().map(AttackEditorApi::getCurrent)
				.map(attackMapper::toHttp).toList();

		return ResponseEntity.ok(attacks);
	}

	@Override
	public ResponseEntity<List<EditablePath>> getLevelPaths(String levelId) {
		LevelEditorApi levelEditor = getLevelEditor(levelId);
		if (levelEditor == null) {
			return ResponseEntity.notFound().build();
		}

		List<EditablePath> paths = levelEditor.getCurrentLevel().getPaths().stream().map(pathMapper::toHttp).toList();

		return ResponseEntity.ok(paths);
	}

	@Override
	public ResponseEntity<Void> removeAttack(String levelId, String attackId) {
		LevelEditorApi levelEditor = getLevelEditor(levelId);
		if (levelEditor == null) {
			return ResponseEntity.notFound().build();
		}

		boolean success = levelEditor.removeAttack(attackId);

		return success ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
	}

	@Override
	public ResponseEntity<Void> removePath(String levelId, String pathId) {
		LevelEditorApi levelEditor = getLevelEditor(levelId);
		if (levelEditor == null) {
			return ResponseEntity.notFound().build();
		}

		boolean success = levelEditor.removePath(pathId);

		return success ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
	}

	@Override
	public ResponseEntity<Void> removeTowerTypeFromLevel(String levelId, String towerTypeId) {
		LevelEditorApi levelEditor = getLevelEditor(levelId);
		if (levelEditor == null) {
			return ResponseEntity.notFound().build();
		}

		boolean success = levelEditor.removeTowerCapacity(towerTypeId);

		return success ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
	}

	@Override
	public ResponseEntity<Void> removeWave(String levelId, String attackId, String waveId) {
		LevelEditorApi levelEditor = getLevelEditor(levelId);
		if (levelEditor == null) {
			return ResponseEntity.notFound().build();
		}

		try {
			AttackEditorApi attackEditor = levelEditor.getAttackEditor(attackId);
			boolean success = attackEditor.removeEditableWave(waveId);

			return success ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();

		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@Override
	public ResponseEntity<EditableLevel> updateLevelMetadata(String levelId,
			@Valid LevelMetadataUpdate levelMetadataUpdate) {
		LevelEditorApi levelEditor = getLevelEditor(levelId);
		if (levelEditor == null) {
			return ResponseEntity.notFound().build();
		}

		if (levelMetadataUpdate.getStartingMoney() != null) {
			levelEditor.attributeInitialMoney(levelMetadataUpdate.getStartingMoney());
		}

		if (levelMetadataUpdate.getStartingLives() != null) {
			levelEditor.attributeInitialLives(levelMetadataUpdate.getStartingLives());
		}

		EditableLevel updatedLevel = levelMapper.toHttp(levelEditor.getCurrentLevel());
		return ResponseEntity.ok(updatedLevel);
	}

	private LevelEditorApi getLevelEditor(String levelId) {
		com.towerdefense.editor.api.model.draft.EditableLevel level = editorApi().getEditableLevel(levelId);
		if (level == null) {
			return null;
		}
		// Reconstruct a LevelEditorApi from the existing rank
		return new com.towerdefense.editor.implementation.LevelEditorApiImpl(level);
	}

	@Override
	public ResponseEntity<Void> addTowerTypeToLevel(String levelId,
			@Valid AddTowerTypeToLevelRequest addTowerTypeToLevelRequest) {

		LevelEditorApi levelEditor = getLevelEditor(levelId);
		if (levelEditor == null) {
			return ResponseEntity.notFound().build();
		}

		boolean success = levelEditor.addTowerCapacity(addTowerTypeToLevelRequest.getTowerTypeId(),
				addTowerTypeToLevelRequest.getMaxRank() != null ? addTowerTypeToLevelRequest.getMaxRank() : 999
		// Par défaut, tous les niveaux
		);

		return success ? ResponseEntity.status(HttpStatus.CREATED).build()
				: ResponseEntity.status(HttpStatus.CONFLICT).build();
	}

	@Override
	public ResponseEntity<Void> updateTowerCapacity(String levelId, String towerTypeId,
			@Valid UpdateTowerCapacityRequest request) {

		LevelEditorApi levelEditor = getLevelEditor(levelId);
		if (levelEditor == null) {
			return ResponseEntity.notFound().build();
		}

		boolean success = levelEditor.updateTowerCapacity(towerTypeId, request.getMaxRank());

		return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
	}

}