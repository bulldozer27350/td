package com.towerdefense.leveleditor.ui;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.towerdefense.editor.api.LevelEditor;
import com.towerdefense.editor.api.model.EditableLevel;
import com.towerdefense.editor.api.model.LevelMetadata;
import com.towerdefense.editor.api.model.PathDefinition;
import com.towerdefense.editor.core.DefaultLevelEditor;
import com.towerdefense.editor.core.GenericIOServices;
import com.towerdefense.editor.core.exporter.LevelExportService;
import com.towerdefense.editor.core.exporter.editable.EditableGenericExportService;
import com.towerdefense.editor.core.importer.LevelImportService;
import com.towerdefense.editor.core.importer.editable.EditableGenericImportService;
import com.towerdefense.editor.core.persistence.EditableGenericObjectJsonIO;
import com.towerdefense.editor.core.persistence.LevelJsonIO;
import com.towerdefense.engine.api.model.configuration.LevelConfig;
import com.towerdefense.leveleditor.ui.render.AsciiLevelRenderer;

public class EditorController {

	private LevelEditor editor;
	private final AsciiLevelRenderer renderer;
	private final LevelJsonIO jsonIO;
	private final LevelImportService importService;
	private final LevelExportService exportService;

	Map<Class, GenericIOServices> genericEditableExportServices = new HashMap<>();

	public EditorController(List<Class<?>> classes, LevelJsonIO jsonIO, LevelImportService importService,
			LevelExportService exportService) {

		for (Class<?> clazz : classes) {
			this.genericEditableExportServices.put(clazz, new GenericIOServices<>(clazz));
		}

		this.renderer = new AsciiLevelRenderer();
		this.jsonIO = jsonIO;
		this.importService = importService;
		this.exportService = exportService;
	}

	private void setEditor(LevelEditor editor) {
		this.editor = editor;
	}

	public void printLevel() {
		System.out.println(renderer.render(this.editor.getCurrentLevel()));
	}

	public void addPath(PathDefinition path) {
		this.editor.addPath(path);
	}

	public void saveLevel(String filePath) throws IOException {
		LevelConfig config = exportService.export(editor.getCurrentLevel());
		jsonIO.save(config, Path.of(filePath));
		System.out.println("Level saved to " + filePath);
	}

	public EditableLevel loadLevel(String filePath) throws IOException {
		LevelConfig config = jsonIO.load(Path.of(filePath));
		EditableLevel level = importService.importLevel(config);
		LevelEditor editor = new DefaultLevelEditor(level.getMetadata(), level.getMap().getWidth(),
				level.getMap().getHeight());
		this.setEditor(editor);
		editor.loadLevel(level);
		System.out.println("Level loaded from " + filePath);
		return level;
	}

	public EditableLevel loadDraftLevel(String filePath) throws IOException {
		EditableLevel currentlevel = (EditableLevel) this.genericEditableExportServices.get(EditableLevel.class).getJsonIO().load(Path.of(filePath));
		EditableLevel level = (EditableLevel) this.genericEditableExportServices.get(EditableLevel.class).getImportService().importEditable(currentlevel);
		LevelEditor editor = new DefaultLevelEditor(level.getMetadata(), level.getMap().getWidth(),
				level.getMap().getHeight());
		this.setEditor(editor);
		editor.loadLevel(level);
		System.out.println("Draft level loaded from " + filePath);
		return level;
	}

	public void createNewLevel(LevelPrimaryData data) {
		LevelPrimaryData primaryData = data;
		LevelMetadata metadata = new LevelMetadata(primaryData.levelId(), primaryData.name(), primaryData.description(),
				primaryData.startingMoney(), primaryData.startingLives());
		LevelEditor internalEditor = new DefaultLevelEditor(metadata, primaryData.width(), primaryData.height());
		this.setEditor(internalEditor);
	}

	public void saveDraftLevel(String filePath) throws IOException {
		EditableGenericExportService<EditableLevel> editableLevelExportService = this.genericEditableExportServices
				.get(EditableLevel.class).getExportService();
		EditableGenericObjectJsonIO<EditableLevel> editableLevelJsonIO = this.genericEditableExportServices.get(EditableLevel.class).getJsonIO();
		editableLevelExportService.export(editor.getCurrentLevel());
		editableLevelJsonIO.save(editor.getCurrentLevel(), Path.of(filePath));
		System.out.println("Draft level saved to " + filePath);

	}
}
