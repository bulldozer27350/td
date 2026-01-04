package com.towerdefense.leveleditor.ui.cli;

import java.util.Scanner;

import com.towerdefense.leveleditor.ui.context.EditorContext;
import com.towerdefense.leveleditor.ui.render.AsciiEnemyTypeTableRenderer;

public class EnemyTypeCli {

	private final Scanner scanner;
	private final AsciiEnemyTypeTableRenderer renderer;
	
	public EnemyTypeCli(Scanner scanner) {
		this.scanner=scanner;
		renderer = new AsciiEnemyTypeTableRenderer();
	}

	public void start(EditorContext editorContext) {
		boolean editing = true;
		while (editing) {
			System.out.println("""
					Enemy management. Your available actions are:
					1. Create enemy type
					2. Remove enemy type
					3. Show enemy types
					4. Save draft enemy type
					5. Save exportable enemy type
					6. Load draft enemy type
					7. Load exportable enemy type
					8. Exit enemy management.
					Your choice?""");
			String cmd = scanner.nextLine();
			switch(cmd) {
			case "1" -> createEnemyType(editorContext);
			case "2" -> removeEnemyType(editorContext);
			case "3" -> showEnemyTypes(editorContext);
			case "4" -> saveDraftEnemyTypes(editorContext);
			case "5" -> saveExportableEnemyTypes(editorContext);
			case "6" -> loadDraftEnemyTypes(editorContext);
			case "7" -> loadExportableEnemyTypes(editorContext);
			case "8" -> editing = false;
			}
		}
	}

	private void loadExportableEnemyTypes(EditorContext editorContext) {
		System.out.println("Path of exportable enemy type to load?");
		String path = this.scanner.nextLine();
		editorContext.getTowerDefenseApi().loadExportableEnemyType(path);
	}

	private void loadDraftEnemyTypes(EditorContext editorContext) {
		System.out.println("Path of draft enemy type to load?");
		String path = this.scanner.nextLine();
		editorContext.getTowerDefenseApi().loadDraftEnemyType(path);
	}

	private void saveExportableEnemyTypes(EditorContext editorContext) {
		System.out.println("Id of exportable enemy type to save?");
		String id = this.scanner.nextLine();
		System.out.println("Path of exportable enemy type to save?");
		String path = this.scanner.nextLine();
		editorContext.getTowerDefenseApi().saveExportableEnemyType(id, path);
	}

	private void saveDraftEnemyTypes(EditorContext editorContext) {
		System.out.println("Id of draft enemy type to save?");
		String id = this.scanner.nextLine();
		System.out.println("Path of draft enemy type to save?");
		String path = this.scanner.nextLine();
		editorContext.getTowerDefenseApi().saveExportableEnemyType(id, path);
	}

	private void showEnemyTypes(EditorContext editorContext) {
		System.out.println(renderer.render(editorContext.getTowerDefenseApi().getAllEditableEnemyTypes()));
	}

	private void removeEnemyType(EditorContext editorContext) {
		System.out.println("Enemy type id? (string)");
		String id = scanner.nextLine();
		editorContext.getTowerDefenseApi().removeEnemyType(id);
	}

	private void createEnemyType(EditorContext editorContext) {
		System.out.println("Enemy type id? (string)");
		String id = scanner.nextLine();
		System.out.println("Enemy type health? (integer)");
		int health = Integer.parseInt(scanner.nextLine());
		System.out.println("Enemy type speed (case/tick)? (double)");
		double speed = Double.parseDouble(scanner.nextLine());
		System.out.println("Enemy type reward? (integer)");
		int reward = Integer.parseInt(scanner.nextLine());
		editorContext.getTowerDefenseApi().createEnemyType(id, health, speed, reward);
	}

}
