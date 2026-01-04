package com.towerdefense.leveleditor.ui.cli;

import java.util.Scanner;

import com.towerdefense.editor.api.level.LevelEditorApi;
import com.towerdefense.leveleditor.ui.context.EditorContext;

public class TowerDefenseStudioCli {

	private Scanner scanner = new Scanner(System.in);

	public void start(EditorContext editorContext) {
		while (true) {
			System.out.println("""
					Welcome on tower defense studio Cli.
					Your available actions are:
					1. Create level.
					2. Load draft level.
					3. Load exportable level.
					4. Save level as draft.
					5. Save level as exportable.
					6. Manage enemy types.
					7. Manage tower types.
					8. Exit application.
					
					Your choice?""");
			switch (scanner.nextLine()) {
			case "1" -> createLevel(editorContext);
			case "2" -> loadDraftLevel(editorContext);
			case "3" -> loadExportableLevel(editorContext);
			case "4" -> saveDraftLevel(editorContext);
			case "5" -> saveExportableLevel(editorContext);
			case "6" -> new EnemyTypeCli(scanner).start(editorContext);
			case "7" -> new TowerTypeCli(scanner).start(editorContext);
			case "8" -> {System.out.println("Goobye!"); System.exit(0);}
			default -> System.out.println("Unknown command!");
			}
		}
	}
	
	private void saveDraftLevel(EditorContext editorContext) {
		System.out.println("Id of level you want to save");
		String id = scanner.nextLine();
		System.out.println("Path of where you want to save it?");
		String path = scanner.nextLine();
		editorContext.getTowerDefenseApi().saveDraftLevel(id, path);
	}
	
	private void saveExportableLevel(EditorContext editorContext) {
		System.out.println("Id of level you want to save");
		String id = scanner.nextLine();
		System.out.println("Path of where you want to save it?");
		String path = scanner.nextLine();
		editorContext.getTowerDefenseApi().saveExportableLevel(id, path);
	}

	private void loadDraftLevel(EditorContext editorContext) {
		System.out.println("Load an existing draft level.");
		System.out.println("Path of the existing level ? (String: be careful to escape \\ characters)");
		String path = scanner.nextLine();
		LevelEditorApi levelEditorApi = editorContext.getTowerDefenseApi().loadDraftLevel(path);
		new LevelEditorCli(scanner).start(levelEditorApi);
	}
	
	private void loadExportableLevel(EditorContext editorContext) {
		System.out.println("Load an existing exportable level.");
		System.out.println("Path of the existing level ? (String: be careful to escape \\ characters)");
		String path = scanner.nextLine();
		LevelEditorApi levelEditorApi = editorContext.getTowerDefenseApi().loadExportableLevel(path);
		new LevelEditorCli(scanner).start(levelEditorApi);
	}

	private void createLevel(EditorContext editorContext) {
		System.out.println("""
				Creation of a new tower defense level.
				Level identifier ? (String)""");
		String levelId = scanner.nextLine();
		
		System.out.println("Level width ? (integer)");
		int width = Integer.parseInt(scanner.nextLine());
		
		System.out.println("Level height ? (integer)");
		int height = Integer.parseInt(scanner.nextLine());
		
		LevelEditorApi levelEditorApi = editorContext.getTowerDefenseApi().createLevel(levelId, width, height);
		new LevelEditorCli(scanner).start(levelEditorApi);
	}
	
}
