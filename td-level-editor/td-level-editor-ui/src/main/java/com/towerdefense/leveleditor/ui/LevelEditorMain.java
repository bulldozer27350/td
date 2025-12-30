package com.towerdefense.leveleditor.ui;

import com.towerdefense.editor.core.exporter.EditableLevelExportService;
import com.towerdefense.editor.core.exporter.LevelExportService;
import com.towerdefense.editor.core.importer.EditableLevelImportService;
import com.towerdefense.editor.core.importer.LevelImportService;
import com.towerdefense.editor.core.persistence.EditableLevelJsonIO;
import com.towerdefense.editor.core.persistence.LevelJsonIO;
import com.towerdefense.editor.core.validation.DefaultEditableLevelValidator;
import com.towerdefense.editor.core.validation.DefaultLevelValidator;

public class LevelEditorMain {

	public static void main(String[] args) {
		EditorController controller = new EditorController(new LevelJsonIO(), new EditableLevelJsonIO(),
				new LevelImportService(), new LevelExportService(new DefaultLevelValidator()),
				new EditableLevelExportService(new DefaultEditableLevelValidator()), new EditableLevelImportService());
		EditorCli editorCli = new EditorCli();
		editorCli.start(controller);
	}
}
