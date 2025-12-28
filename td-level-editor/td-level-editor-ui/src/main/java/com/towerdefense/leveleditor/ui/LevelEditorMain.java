package com.towerdefense.leveleditor.ui;

import com.towerdefense.editor.core.exporter.LevelExportService;
import com.towerdefense.editor.core.exporter.LevelJsonIO;
import com.towerdefense.editor.core.importer.LevelImportService;
import com.towerdefense.editor.core.validation.DefaultLevelValidator;

public class LevelEditorMain {

	public static void main(String[] args) {
		EditorController controller = new EditorController(new LevelJsonIO(), new LevelImportService(),
				new LevelExportService(new DefaultLevelValidator()));
		EditorCli editorCli = new EditorCli();
		editorCli.start(controller);
	}
}
