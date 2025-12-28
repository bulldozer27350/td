package com.towerdefense.leveleditor.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.towerdefense.editor.api.model.PathDefinition;
import com.towerdefense.editor.api.model.PositionDefinition;

public class EditorCli {

	public EditorCli() {
	}

	public void start(EditorController controller) {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("You can either start a new level, load an existing one, or leave.");
			System.out.println("1. New Level");
			System.out.println("2. Load Level");
			System.out.println("3. Exit application");

			System.out.println("Your choice ? (integer)");
			String cmd = scanner.nextLine();
			switch (cmd) {
			case "1" -> {
				System.out.println("Starting a new Level...");
				LevelPrimaryData data = initialize();
				controller.createNewLevel(data);
				this.continueCreation(controller);
			}
			case "2" -> {
				System.out.println("Loading an existing Level...");
				if (!this.load(controller, scanner)) {
					System.out.println("Loading failed.");
				}
			}
			case "3" -> System.exit(0);
			default -> System.out.println("Unknown command");
			}
		}
	}

	private void continueCreation(EditorController controller) {
		Scanner scanner = new Scanner(System.in);
		boolean running = true;
		while (running) {
			System.out.println("Available commands:");
			System.out.println("1. show");
			System.out.println("2. add path");
			System.out.println("3. save");
			System.out.println("4. exit level editing session (all unsaved changes will be lost)");
			System.out.println("Your choice ? (integer)");
			String cmd = scanner.nextLine();

			switch (cmd) {
			case "1" -> controller.printLevel();
			case "2" -> addPath(controller, scanner);
			case "3" -> save(controller, scanner);
			case "4" -> {
				System.out.println("Exiting this level editing session.");
				running = false;
			}
			default -> System.out.println("Unknown command");
			}
		}
	}

	private void addPath(EditorController controller, Scanner scanner) {
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
			controller.addPath(new PathDefinition(pathId, points));
		} catch (Exception e) {
			System.out.println("Invalid input format.");
		}
	}

	private LevelPrimaryData initialize() {
		Scanner scanner = new Scanner(System.in);
		LevelPrimaryData dimensions;
		while (true) {
			System.out.println("width (integer) ?");
			String cmd = scanner.nextLine();
			int width = Integer.parseInt(cmd);
			System.out.println("height (integer) ?");
			cmd = scanner.nextLine();
			int height = Integer.parseInt(cmd);
			System.out.println("level id (integer) ?");
			cmd = scanner.nextLine();
			int levelId = Integer.parseInt(cmd);
			System.out.println("level name (string) ?");
			cmd = scanner.nextLine();
			String levelName = cmd;
			System.out.println("level description (string) ?");
			cmd = scanner.nextLine();
			String levelDescription = cmd;
			System.out.println("recommanded difficulty (integer) ?");
			cmd = scanner.nextLine();
			int difficulty = Integer.parseInt(cmd);
			System.out.println("player starting money (integer) ?");
			cmd = scanner.nextLine();
			int startingMoney = Integer.parseInt(cmd);
			System.out.println("player starting lives (integer) ?");
			cmd = scanner.nextLine();
			int startingLives = Integer.parseInt(cmd);
			dimensions = new LevelPrimaryData(width, height, levelId, levelName, levelDescription, difficulty,
					startingMoney, startingLives);
			break;
		}
		return dimensions;
	}

	private boolean save(EditorController controller, Scanner scanner) {
		System.out.print("File path to save (e.g. level1.json) ? ");
		String filePath = scanner.nextLine();
		try {
			controller.saveLevel(filePath);
			return true;
		} catch (IOException e) {
			System.out.println("Impossible to access " + filePath);
			return false;
		}
	}

	private boolean load(EditorController controller, Scanner scanner) {
		System.out.print("File path to load ? ");
		String filePath = scanner.nextLine();
		try {
			controller.loadLevel(filePath);
			return true;
		} catch (IOException e) {
			System.out.println("Impossible to access " + filePath);
			return false;
		}
	}
}
