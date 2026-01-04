package com.towerdefense.leveleditor.ui.cli;

import java.util.Scanner;

import com.towerdefense.editor.api.level.AttackEditorApi;

public class AttackEditorCli {

	private final Scanner scanner;

	public AttackEditorCli(Scanner scanner) {
		this.scanner = scanner;
	}

	public void start(AttackEditorApi attackEditor) {
		boolean editing = true;
		if (editing) {
			System.out.println("""
					Attack management. Your available actions are:
					1. Add wave
					2. Remove wave
					3. Show waves
					4. Exit attack management
					Your choice?""");
			String cmd = scanner.nextLine();
			switch (cmd) {
			case "1" -> addWave(attackEditor);
			case "2" -> removeWave(attackEditor);
			case "3" -> showWaves(attackEditor);
			case "4" -> editing = false;
			}
		}
	}

	private void addWave(AttackEditorApi attackEditorApi) {
		System.out.println("Id of the wave?");
		String id = scanner.nextLine();
		System.out.println("Start tick?");
		int startTick = Integer.parseInt(scanner.nextLine());
		System.out.println("Interval between spawns? (ticks number)");
		int spawnInterval = Integer.parseInt(scanner.nextLine());
		System.out.println("How much ennemies in the wave?");
		int count = Integer.parseInt(scanner.nextLine());
		System.out.println("Enemy type id?");
		String enemyTypeId = scanner.nextLine();
		System.out.println("Path used by the wave?");
		String pathId = scanner.nextLine();
		attackEditorApi.addEditableWave(id, startTick, spawnInterval, count, enemyTypeId, pathId);
	}
	private void removeWave(AttackEditorApi attackEditorApi) {
		System.out.println("Id of the wave you want to delete?");
		String id = scanner.nextLine();
		attackEditorApi.removeEditableWave(id);
	}
	private void showWaves(AttackEditorApi attackEditorApi) {
		System.out.println("NOT YET AVAILABLE ...");
	}

}
