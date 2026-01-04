package com.towerdefense.editor.implementation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.towerdefense.editor.api.model.draft.EditableTowerType;
import com.towerdefense.editor.api.tower.TowerTypeEditorApi;
import com.towerdefense.editor.core.GenericIOServices;
import com.towerdefense.editor.core.exporter.TowerTypeExportService;
import com.towerdefense.editor.core.importer.TowerTypeImportService;
import com.towerdefense.editor.core.persistence.TowerTypeJsonIO;
import com.towerdefense.editor.itf.TowerTypesCollectionEditorApi;
import com.towerdefense.engine.api.model.configuration.TowerTypeConfig;

public class TowerTypesCollectionEditorApiImpl implements TowerTypesCollectionEditorApi {

	private final List<EditableTowerType> editableTowerTypes;
	private final GenericIOServices<EditableTowerType> draftIoServices;

	private final TowerTypeExportService exportService;
	private final TowerTypeImportService importService;
	private final TowerTypeJsonIO towerTypeJsonIo;

	public TowerTypesCollectionEditorApiImpl(GenericIOServices<EditableTowerType> draftIoServices,
			TowerTypeExportService towerTypeExportService, TowerTypeImportService towerTypeImportService,
			TowerTypeJsonIO towerTypeJsonIo) {
		this.editableTowerTypes = new ArrayList<>();
		this.draftIoServices = draftIoServices;

		this.exportService = towerTypeExportService;
		this.importService = towerTypeImportService;
		this.towerTypeJsonIo = towerTypeJsonIo;
	}

	@Override
	public TowerTypeEditorApi createTowerType(String id, String name) {
		EditableTowerType towerType = new EditableTowerType(id, name);
		this.editableTowerTypes.add(towerType);
		return new TowerTypeEditorApiImpl(towerType);
	}

	@Override
	public TowerTypeEditorApi loadDraftTowerType(String path) {
		try {
			EditableTowerType importEditable = this.draftIoServices.getImportService()
					.importEditable(this.draftIoServices.getJsonIO().load(Path.of(path)));
			this.editableTowerTypes.add(importEditable);
			return new TowerTypeEditorApiImpl(importEditable);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public TowerTypeEditorApi loadExportableTowerType(String path) {
		try {
			EditableTowerType importTowerType = this.importService
					.importLevel(this.towerTypeJsonIo.load(Path.of(path)));
			this.editableTowerTypes.add(importTowerType);
			return new TowerTypeEditorApiImpl(importTowerType);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean removeTowerType(String id) {
		return editableTowerTypes.removeIf(e -> e.id().equalsIgnoreCase(id));
	}

	@Override
	public boolean saveDraftTowerType(String id, String path) {
		boolean success = false;
		EditableTowerType editableTowerType = this.editableTowerTypes.stream().filter(e -> e.id().equalsIgnoreCase(id))
				.findFirst().orElse(null);
		if (editableTowerType != null) {
			this.draftIoServices.getExportService().export(editableTowerType);
			try {
				this.draftIoServices.getJsonIO().save(editableTowerType, Path.of(path));
				// No success by default. Only this path is meaning operation succeeded.
				success = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return success;
	}

	@Override
	public boolean saveExportableTowerType(String id, String path) {
		boolean success = false;
		EditableTowerType editableTowerType = this.editableTowerTypes.stream().filter(e -> e.id().equalsIgnoreCase(id))
				.findFirst().orElse(null);
		if (editableTowerType != null) {
			TowerTypeConfig config = this.exportService.export(editableTowerType);
			try {
				this.towerTypeJsonIo.save(config, Path.of(path));
				// No success by default. Only this path is meaning operation succeeded.
				success = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return success;
	}

	@Override
	public TowerTypeEditorApi getEditableTowerType(String id) {
		return new TowerTypeEditorApiImpl(
				this.editableTowerTypes.stream().filter(e -> e.id().equalsIgnoreCase(id)).findFirst().orElse(null));
	}

	@Override
	public List<EditableTowerType> getAllEditableTowerTypes() {
		return new ArrayList<EditableTowerType>(this.editableTowerTypes);
	}

}
