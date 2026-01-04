package com.towerdefense.leveleditor.ui.cli;

import java.util.Scanner;

import com.towerdefense.editor.api.tower.TowerTypeEditorApi;
import com.towerdefense.leveleditor.ui.context.EditorContext;
import com.towerdefense.leveleditor.ui.render.AsciiTowerTypeRenderer;

public class TowerTypeCli {

	private Scanner scanner;
	private final AsciiTowerTypeRenderer renderer;
	
	public TowerTypeCli(Scanner scanner) {
		this.scanner = scanner;
		this.renderer = new AsciiTowerTypeRenderer();
	}

	public void start(EditorContext editorContext) {
		boolean editing = true;
		while (editing) {
			System.out.println("""
					Tower management. Your available actions are:
					1. Create tower type
					2. Remove tower type
					3. Show tower types
					4. Load draft tower types
					5. Load exportable tower types
					6. Save as draft tower types
					7. Save as exportable tower types
					8. Exit tower management
					Your choice?""");
			String cmd = scanner.nextLine();
			switch(cmd) {
			case "1" -> createTowerType(editorContext);
			case "2" -> removeTowerType(editorContext);
			case "3" -> showTowerTypes(editorContext);
			case "4" -> loadDraftTowerTypes(editorContext);
			case "5" -> loadExportableTowerTypes(editorContext);
			case "6" -> saveDraftTowerTypes(editorContext);
			case "7" -> saveExportableTowerTypes(editorContext);
			case "8" -> editing = false;
			}
		}
	}

	private void saveExportableTowerTypes(EditorContext editorContext) {
		System.out.println("Path of exportable tower type to save?");
		String path = this.scanner.nextLine();
		TowerTypeEditorApi towerTypeEditorApi = editorContext.getTowerDefenseApi().loadExportableTowerType(path);
		new TowerLevelEditorCli(scanner).start(towerTypeEditorApi);
	}

	private void saveDraftTowerTypes(EditorContext editorContext) {
		System.out.println("Id of draft tower type to save?");
		String id = this.scanner.nextLine();
		System.out.println("Path of draft tower type to save?");
		String path = this.scanner.nextLine();
		editorContext.getTowerDefenseApi().saveDraftTowerType(id, path);
	}

	private void loadExportableTowerTypes(EditorContext editorContext) {
		System.out.println("Id of exportable tower type to save?");
		String id = this.scanner.nextLine();
		System.out.println("Path of exportable tower type to save?");
		String path = this.scanner.nextLine();
		editorContext.getTowerDefenseApi().saveExportableTowerType(id, path);
	}

	private void loadDraftTowerTypes(EditorContext editorContext) {
		System.out.println("Path of tower type to load?");
		String path = this.scanner.nextLine();
		TowerTypeEditorApi towerTypeEditorApi = editorContext.getTowerDefenseApi().loadDraftTowerType(path);
		new TowerLevelEditorCli(scanner).start(towerTypeEditorApi);
	}

	private void showTowerTypes(EditorContext editorContext) {
		System.out.println(this.renderer.render(editorContext.getTowerDefenseApi().getAllEditableTowerTypes()));
	}

	private void removeTowerType(EditorContext editorContext) {
		System.out.println("Tower type id? (string)");
		String id = scanner.nextLine();
		editorContext.getTowerDefenseApi().removeTowerType(id);
	}

	private void createTowerType(EditorContext editorContext) {
		System.out.println("Tower type id? (String)");
		String id = scanner.nextLine();
		System.out.println("Tower type name? (String)");
		String name = scanner.nextLine();
		TowerTypeEditorApi towerTypeEditorApi = editorContext.getTowerDefenseApi().createTowerType(id, name);
		new TowerLevelEditorCli(scanner).start(towerTypeEditorApi);
	}

}
