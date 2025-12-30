package com.towerdefense.leveleditor.ui;

import java.io.IOException;
import java.nio.file.Path;

import com.towerdefense.editor.api.LevelEditor;
import com.towerdefense.editor.api.model.EditableLevel;
import com.towerdefense.editor.api.model.LevelMetadata;
import com.towerdefense.editor.api.model.PathDefinition;
import com.towerdefense.editor.core.DefaultLevelEditor;
import com.towerdefense.editor.core.exporter.EditableLevelExportService;
import com.towerdefense.editor.core.exporter.LevelExportService;
import com.towerdefense.editor.core.importer.EditableLevelImportService;
import com.towerdefense.editor.core.importer.LevelImportService;
import com.towerdefense.editor.core.persistence.EditableLevelJsonIO;
import com.towerdefense.editor.core.persistence.LevelJsonIO;
import com.towerdefense.engine.api.model.configuration.LevelConfig;
import com.towerdefense.leveleditor.ui.render.AsciiLevelRenderer;

public class EditorController {

	private LevelEditor editor;
	private final AsciiLevelRenderer renderer;
	private final LevelJsonIO jsonIO;
	private final EditableLevelJsonIO editableLevelJsonIO;
	private final LevelImportService importService;
	private final LevelExportService exportService;
	private final EditableLevelExportService editableLevelExportService;
	private final EditableLevelImportService editableLevelImportService;

	public EditorController(LevelJsonIO jsonIO, EditableLevelJsonIO editableLevelJsonIO,
			LevelImportService importService, LevelExportService exportService,
			EditableLevelExportService editableLevelExportService, EditableLevelImportService editableLevelImportService) {
		this.renderer = new AsciiLevelRenderer();
		this.jsonIO = jsonIO;
		this.editableLevelJsonIO = editableLevelJsonIO;
		this.importService = importService;
		this.exportService = exportService;
		this.editableLevelExportService = editableLevelExportService;
		this.editableLevelImportService = editableLevelImportService;
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
		LevelEditor editor = new DefaultLevelEditor(level.getMetadata(), level.getMap().getWidth(), level.getMap().getHeight());
		this.setEditor(editor);
		editor.loadLevel(level);
		System.out.println("Level loaded from " + filePath);
		return level;
	}
	
	public EditableLevel loadDraftLevel(String filePath) throws IOException {
		EditableLevel currentlevel = editableLevelJsonIO.load(Path.of(filePath));
		EditableLevel level = editableLevelImportService.importLevel(currentlevel);
		LevelEditor editor = new DefaultLevelEditor(level.getMetadata(), level.getMap().getWidth(), level.getMap().getHeight());
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
		this.editableLevelExportService.export(editor.getCurrentLevel());
		editableLevelJsonIO.save(editor.getCurrentLevel(), Path.of(filePath));
		System.out.println("Draft level saved to " + filePath);

	}
}
