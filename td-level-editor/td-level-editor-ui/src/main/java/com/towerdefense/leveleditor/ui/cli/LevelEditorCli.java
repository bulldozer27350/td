package com.towerdefense.leveleditor.ui.cli;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.towerdefense.editor.api.level.LevelEditorApi;
import com.towerdefense.editor.api.model.draft.EditableAttack;
import com.towerdefense.editor.api.model.draft.EditablePath;
import com.towerdefense.editor.api.model.exportable.PositionDefinition;
import com.towerdefense.leveleditor.ui.render.AsciiLevelRenderer;

public class LevelEditorCli {

	private final Scanner scanner;
	private final AsciiLevelRenderer renderer;

	public LevelEditorCli(Scanner scanner) {
		this.scanner = scanner;
		this.renderer = new AsciiLevelRenderer();
	}

	public void start(LevelEditorApi levelEditorApi) {
		boolean editing = true;
		while (editing) {
			System.out.println("""
					Level management menu.
					Your available actions are:
					1. Show current level
					2. Add path
					3. Add attack
					4. Add tower availability
					5. Change initial money
					6. Change initial available lives
					7. Back to main tower defense studio Cli (unsaved modifications will be lost)
					Your choice?""");
			String cmd = scanner.nextLine();
			switch (cmd) {
			case "1" -> System.out.println(renderer.render(levelEditorApi.getCurrentLevel()));
			case "2" -> addPath(levelEditorApi);
			case "3" -> addAttack(levelEditorApi);
			case "4" -> addTowerAvailability(levelEditorApi);
			case "5" -> setInitialMoney(levelEditorApi);
			case "6" -> setInitialLives(levelEditorApi);
			case "7" -> {
				System.out.println("Back to main menu");
				editing = false;
			}
			default -> System.out.println("Unknown command!");
			}
		}

	}

	private void setInitialMoney(LevelEditorApi levelEditorApi) {
		System.out.println("How much money should be available when starting level? (integer)");
		int money = Integer.parseInt(scanner.nextLine());
		levelEditorApi.attributeInitialMoney(money);
	}

	private void setInitialLives(LevelEditorApi levelEditorApi) {
		System.out.println("How many lives should be available when starting level? (integer)");
		int lives = Integer.parseInt(scanner.nextLine());
		levelEditorApi.attributeInitialLives(lives);
	}

	private void addTowerAvailability(LevelEditorApi levelEditorApi) {
		System.out.println("Tower type id ? (String)");
		String id = scanner.nextLine();
		levelEditorApi.addTowerType(id);
	}

	private void addAttack(LevelEditorApi levelEditorApi) {
		System.out.println("Attack identifier ? (String)");
		String id = scanner.nextLine();
		levelEditorApi.addAttack(new EditableAttack(id));
		new AttackEditorCli(scanner).start(levelEditorApi.getAttackEditor(id));
	}

	private void addPath(LevelEditorApi levelEditorApi) {
		System.out.println("Adding a new path.");
		System.out.print("Path id (string) ? ");
		String pathId = scanner.nextLine();

		System.out.println("Enter path points as x1,y1;x2,y2;...");
		String cmd = scanner.nextLine();

		try {
			List<PositionDefinition> points = new ArrayList<>();
			for (String pointStr : cmd.split(";")) {
				String[] coords = pointStr.split(",");
				points.add(
						new PositionDefinition(Integer.parseInt(coords[0].trim()), Integer.parseInt(coords[1].trim())));
			}
			levelEditorApi.addPath(new EditablePath(pathId, points));
		} catch (Exception e) {
			System.out.println("Invalid input format.");
		}
	}

}
