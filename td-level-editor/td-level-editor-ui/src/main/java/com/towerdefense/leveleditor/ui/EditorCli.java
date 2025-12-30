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
			System.out.println("3. Load DRAFT Level");
			System.out.println("4. Exit application");

			System.out.println("Your choice ? (integer)");
			String cmd = scanner.nextLine();
			switch (cmd) {
			case "1" -> {
				System.out.println("Starting a new Level...");
				LevelPrimaryData data = initialize(scanner);
				controller.createNewLevel(data);
				this.continueCreation(controller);
			}
			case "2" -> {
				System.out.println("Loading an existing Level...");
				if (!this.load(controller, scanner)) {
					System.out.println("Loading failed.");
				}
			}
			case "3" -> {
				System.out.println("Loading an existing DRAFT Level...");
				if (!this.loadDraft(controller, scanner)) {
					System.out.println("Loading failed.");
				}
			}
			case "4" -> System.exit(0);
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
			System.out.println("3. save this draft");
			System.out.println("4. save as exportable level");
			System.out.println("5. exit level editing session (all unsaved changes will be lost)");
			System.out.println("Your choice ? (integer)");
			String cmd = scanner.nextLine();

			switch (cmd) {
			case "1" -> controller.printLevel();
			case "2" -> addPath(controller, scanner);
			case "3" -> saveDraft(controller, scanner);
			case "4" -> save(controller, scanner);
			case "5" -> {
				System.out.println("Exiting this level editing session.");
				running = false;
			}
			default -> System.out.println("Unknown command");
			}
		}
	}

	private boolean saveDraft(EditorController controller, Scanner scanner) {
		System.out.print("DRAFT SAVE - File path to save (e.g. level1.json) ? ");
		String filePath = scanner.nextLine();
		try {
			controller.saveDraftLevel(filePath);
			return true;
		} catch (IOException e) {
			System.out.println("Impossible to access " + filePath);
			return false;
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

	private LevelPrimaryData initialize(Scanner scanner) {
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
			System.out.println("player starting money (integer) ?");
			cmd = scanner.nextLine();
			int startingMoney = Integer.parseInt(cmd);
			System.out.println("player starting lives (integer) ?");
			cmd = scanner.nextLine();
			int startingLives = Integer.parseInt(cmd);
			dimensions = new LevelPrimaryData(width, height, levelId, levelName, levelDescription,
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
			this.continueCreation(controller);
			return true;
		} catch (IOException e) {
			System.out.println("Impossible to access " + filePath);
			return false;
		}
	}
	
	private boolean loadDraft(EditorController controller, Scanner scanner) {
		System.out.print("File path to load a draft level ? ");
		String filePath = scanner.nextLine();
		try {
			controller.loadDraftLevel(filePath);
			this.continueCreation(controller);
			return true;
		} catch (IOException e) {
			System.out.println("Impossible to access " + filePath);
			System.out.println(e);
			return false;
		}
	}
}
