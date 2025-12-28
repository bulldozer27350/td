package com.towerdefense.leveleditor.ui;

import java.io.IOException;
import java.nio.file.Path;

import com.towerdefense.editor.api.LevelEditor;
import com.towerdefense.editor.api.model.EditableLevel;
import com.towerdefense.editor.api.model.LevelMetadata;
import com.towerdefense.editor.api.model.PathDefinition;
import com.towerdefense.editor.core.DefaultLevelEditor;
import com.towerdefense.editor.core.exporter.LevelExportService;
import com.towerdefense.editor.core.exporter.LevelJsonIO;
import com.towerdefense.editor.core.importer.LevelImportService;
import com.towerdefense.engine.api.model.configuration.LevelConfig;
import com.towerdefense.leveleditor.ui.render.AsciiLevelRenderer;

public class EditorController {

	private LevelEditor editor;
	private final AsciiLevelRenderer renderer;
	private final LevelJsonIO jsonIO;
	private LevelImportService importService;
	private LevelExportService exportService;

	public EditorController(LevelJsonIO jsonIO, LevelImportService importService, LevelExportService exportService) {
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

	public void loadLevel(String filePath) throws IOException {
		LevelConfig config = jsonIO.load(Path.of(filePath));
        EditableLevel level = importService.importLevel(config);
        editor.loadLevel(level);
		System.out.println("Level loaded from " + filePath);
	}

	public void createNewLevel(LevelPrimaryData data) {
		LevelPrimaryData primaryData = data;
		LevelMetadata metadata = new LevelMetadata(primaryData.levelId(), primaryData.name(), primaryData.description(),
				primaryData.recommendedDifficulty(), primaryData.startingMoney(), primaryData.startingLives());
		LevelEditor editor = new DefaultLevelEditor(metadata, primaryData.width(), primaryData.height());
		this.setEditor(editor);
	}
}
