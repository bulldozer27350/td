package com.towerdefense.leveleditor.ui.cli;

import java.util.Arrays;
import java.util.Scanner;

import com.towerdefense.editor.api.tower.TowerTypeEditorApi;
import com.towerdefense.leveleditor.ui.render.AsciiTowerTypeRenderer;

public class TowerLevelEditorCli {

	private final Scanner scanner;
	private final AsciiTowerTypeRenderer renderer;

	public TowerLevelEditorCli(Scanner scanner) {
		this.scanner = scanner;
		this.renderer = new AsciiTowerTypeRenderer();
	}

	public void start(TowerTypeEditorApi towerTypeEditorApi) {
		boolean editing = true;
		while (editing) {
			System.out.println("""
					Tower type level management. Your available actions are:
					1. Add tower type level
					2. Remove tower type level
					3. Show tower type levels
					4. Exit tower type levels management.
					Your choice?""");
			String cmd = scanner.nextLine();
			switch(cmd) {
			case "1" -> createTowerTypeLevel(towerTypeEditorApi);
			case "2" -> removeTowerTypeLevel(towerTypeEditorApi);
			case "3" -> showTowerTypeLevels(towerTypeEditorApi);
			case "4" -> editing = false;
			}
		}
	}

	private void showTowerTypeLevels(TowerTypeEditorApi towerTypeEditorApi) {
		System.out.println(this.renderer.render(Arrays.asList(towerTypeEditorApi.getCurrentTowerType())));

	}

	private void removeTowerTypeLevel(TowerTypeEditorApi towerTypeEditorApi) {
		System.out.println("Tower level you want to delete?");
		int level = Integer.parseInt(scanner.nextLine());
		towerTypeEditorApi.removeTowerLevel(level);
	}

	private void createTowerTypeLevel(TowerTypeEditorApi towerTypeEditorApi) {
		System.out.println("Tower level you want to create? (integer)");
		int level = Integer.parseInt(scanner.nextLine());
		System.out.println("Tower damage? (integer)");
		int damage = Integer.parseInt(scanner.nextLine());
		System.out.println("Tower range? (double)");
		double range = Double.parseDouble(scanner.nextLine());
		System.out.println("Tower reload time? (double)");
		double reloadTime = Double.parseDouble(scanner.nextLine());
		System.out.println("Tower cost? (integer)");
		int cost = Integer.parseInt(scanner.nextLine());
		System.out.println("Tower sell price? (integer)");
		int sellReward = Integer.parseInt(scanner.nextLine());
		System.out.println("Construction build time in tricks? (integer)");
		int buildTime = Integer.parseInt(scanner.nextLine());
		towerTypeEditorApi.addTowerLevel(level, damage, reloadTime, cost, range, sellReward, buildTime);
	}

}
