package com.towerdefense.editor.implementation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.towerdefense.editor.api.model.draft.EditableEnemyType;
import com.towerdefense.editor.core.GenericIOServices;
import com.towerdefense.editor.core.exporter.EnemyTypeExportService;
import com.towerdefense.editor.core.importer.EnemyTypeImportService;
import com.towerdefense.editor.core.persistence.EnemyTypeJsonIO;
import com.towerdefense.editor.itf.EnemyTypeEditorApi;
import com.towerdefense.engine.api.model.configuration.EnemyTypeConfig;

public class EnemyTypeEditorApiImpl implements EnemyTypeEditorApi {

	private final List<EditableEnemyType> enemyTypes;
	private final GenericIOServices<EditableEnemyType> draftIoServices;
	
	private final EnemyTypeExportService exportService;
	private final EnemyTypeImportService importService;
	private final EnemyTypeJsonIO enemyTypeJsonIo;

	public EnemyTypeEditorApiImpl(GenericIOServices<EditableEnemyType> draftIoServices,
			EnemyTypeExportService enemyTypeExportService, EnemyTypeImportService enemyTypeImportService,
			EnemyTypeJsonIO enemyTypeJsonIo) {
		this.enemyTypes = new ArrayList<>();
		this.draftIoServices = draftIoServices;
		
		this.exportService = enemyTypeExportService;
		this.importService = enemyTypeImportService;
		this.enemyTypeJsonIo = enemyTypeJsonIo;
	}

	@Override
	public boolean createEnemyType(String id, int hp, double speed, int reward) {
		return this.enemyTypes.add(new EditableEnemyType(id, hp, speed, reward));
	}

	@Override
	public boolean loadDraftEnemyType(String path) {
		try {
			return this.enemyTypes.add(this.draftIoServices.getImportService()
					.importEditable(this.draftIoServices.getJsonIO().load(Path.of(path))));
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public boolean loadExportableEnemyType(String path) {
		try {
			return this.enemyTypes.add(this.importService
					.importEnemyType(this.enemyTypeJsonIo.load(Path.of(path))));
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public boolean removeEnemyType(String id) {
		return this.enemyTypes.removeIf(type -> type.getId().equalsIgnoreCase(id));
	}

	@Override
	public List<EditableEnemyType> getAllEditableEnemyTypes() {
		return new ArrayList<EditableEnemyType>(this.enemyTypes);
	}

	@Override
	public EditableEnemyType getEditableEnemyType(String id) {
		return this.enemyTypes.stream().filter(type -> type.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
	}

	@Override
	public boolean saveDraftEnemyType(String id, String path) {
		boolean success = false;
		EditableEnemyType editableEnemyType = this.getEditableEnemyType(id);
		if (editableEnemyType != null) {
			this.draftIoServices.getExportService().export(editableEnemyType);
			try {
				this.draftIoServices.getJsonIO().save(editableEnemyType, Path.of(path));
				// No success by default. Only this path is meaning operation succeeded.
				success = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return success;
	}

	@Override
	public boolean saveExportableEnemyType(String id, String path) {
		boolean success = false;
		EditableEnemyType editableEnemyType = this.getEditableEnemyType(id);
		if (editableEnemyType != null) {
			EnemyTypeConfig config = this.exportService.export(editableEnemyType);
			try {
				this.enemyTypeJsonIo.save(config, Path.of(path));
				// No success by default. Only this path is meaning operation succeeded.
				success = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return success;
	}

}
