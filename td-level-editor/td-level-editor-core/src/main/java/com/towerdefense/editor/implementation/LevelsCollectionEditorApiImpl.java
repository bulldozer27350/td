package com.towerdefense.editor.implementation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.towerdefense.editor.api.level.LevelEditorApi;
import com.towerdefense.editor.api.model.draft.EditableAttack;
import com.towerdefense.editor.api.model.draft.EditableLevel;
import com.towerdefense.editor.api.model.draft.EditableMap;
import com.towerdefense.editor.api.model.draft.TowerCapacity;
import com.towerdefense.editor.api.model.exportable.LevelMetadata;
import com.towerdefense.editor.core.GenericIOServices;
import com.towerdefense.editor.core.exporter.LevelExportService;
import com.towerdefense.editor.core.importer.LevelImportService;
import com.towerdefense.editor.core.persistence.LevelJsonIO;
import com.towerdefense.editor.itf.LevelsCollectionEditorApi;
import com.towerdefense.engine.api.model.configuration.LevelConfig;

public class LevelsCollectionEditorApiImpl implements LevelsCollectionEditorApi {

	private final List<EditableLevel> editableLevels;
	private final GenericIOServices<EditableLevel> draftIoServices;

	private final LevelExportService exportService;
	private final LevelImportService importService;
	private final LevelJsonIO levelJsonIo;

	public LevelsCollectionEditorApiImpl(GenericIOServices<EditableLevel> draftIoServices,
			LevelExportService levelExportService, LevelImportService levelImportService, LevelJsonIO levelJsonIo) {
		this.editableLevels = new ArrayList<>();
		this.draftIoServices = draftIoServices;

		this.exportService = levelExportService;
		this.importService = levelImportService;
		this.levelJsonIo = levelJsonIo;
	}

	@Override
	public LevelEditorApi createLevel(String id, int width, int height) {
		LevelMetadata metadata = new LevelMetadata(id, 0, 1);
		EditableMap map = new EditableMap(width, height);
		EditableLevel editableLevel = new EditableLevel(metadata, map, new ArrayList<EditableAttack>(),
				new ArrayList<TowerCapacity>());
		this.editableLevels.add(editableLevel);
		return new LevelEditorApiImpl(editableLevel);
	}

	@Override
	public LevelEditorApi loadDraftLevel(String path) {
		try {
			EditableLevel importEditable = this.draftIoServices.getImportService()
					.importEditable(this.draftIoServices.getJsonIO().load(Path.of(path)));
			this.editableLevels.add(importEditable);
			return new LevelEditorApiImpl(importEditable);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public LevelEditorApi loadExportableLevel(String path) {
		try {
			EditableLevel importLevel = this.importService.importLevel(this.levelJsonIo.load(Path.of(path)));
			this.editableLevels.add(importLevel);
			return new LevelEditorApiImpl(importLevel);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean removeLevel(String id) {
		return editableLevels.removeIf(e -> e.getMetadata().getId().equals(id));
	}

	@Override
	public boolean saveDraftLevel(String id, String path) {
		boolean success = false;
		EditableLevel editableLevel = this.getEditableLevel(id);
		if (editableLevel != null) {
			this.draftIoServices.getExportService().export(editableLevel);
			try {
				this.draftIoServices.getJsonIO().save(editableLevel, Path.of(path));
				// No success by default. Only this path is meaning operation succeeded.
				success = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return success;
	}

	@Override
	public boolean saveExportableLevel(String id, String path) {
		boolean success = false;
		EditableLevel editableLevel = this.getEditableLevel(id);
		if (editableLevel != null) {
			LevelConfig config = this.exportService.export(editableLevel);
			try {
				this.levelJsonIo.save(config, Path.of(path));
				// No success by default. Only this path is meaning operation succeeded.
				success = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return success;
	}

	@Override
	public EditableLevel getEditableLevel(String id) {
		return this.editableLevels.stream().filter(e -> e.getMetadata().getId().equalsIgnoreCase(id)).findFirst()
				.orElse(null);

	}

	@Override
	public List<EditableLevel> getAllEditableLevels() {
		return new ArrayList<EditableLevel>(this.editableLevels);
	}

}
