package com.towerdefense.runner;

import java.util.Scanner;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.towerdefense.engine.api.GameEngineApi;
import com.towerdefense.engine.api.model.EnemyDTO;
import com.towerdefense.engine.api.model.ProjectileDTO;
import com.towerdefense.engine.api.model.TowerDTO;
import com.towerdefense.engine.api.model.command.GameCommand;
import com.towerdefense.engine.api.model.command.PlaceTowerCommand;
import com.towerdefense.engine.api.model.configuration.GameConfig;
import com.towerdefense.runner.config.reader.EnemiesConfigProvider;
import com.towerdefense.runner.config.reader.LevelConfigProvider;
import com.towerdefense.runner.config.reader.PathsConfigProvider;
import com.towerdefense.runner.config.reader.TowersConfigProvider;
import com.towerdefense.viewer.ConsoleViewer;
import com.towerdefense.viewer.GameStateAsciiRenderer;
import com.towerdefense.viewer.RendererRegistry;
import com.towerdefense.viewer.renderers.EnemyRenderer;
import com.towerdefense.viewer.renderers.ProjectileRenderer;
import com.towerdefense.viewer.renderers.TowerRenderer;

@Component
/**
 * Classe responsable de l'exécution d'une partie de Tower Defense en mode
 * console.
 */
public class ConsoleGameRunner implements CommandLineRunner {

	private GameEngineApi gameEngine;

	public ConsoleGameRunner(GameEngineApi gameEngineApi) {
		this.gameEngine = gameEngineApi;
	}

	@Override
	/**
	 * Lance une partie de Tower Defense en mode console.
	 */
	public void run(String... args) throws Exception {
		System.out.println("=== Tower Defense Console Runner (Interactive) ===");

	    // Viewer
	    RendererRegistry registry = new RendererRegistry();
	    registry.register(EnemyDTO.class, new EnemyRenderer());
	    registry.register(TowerDTO.class, new TowerRenderer());
	    registry.register(ProjectileDTO.class, new ProjectileRenderer());
	    GameStateAsciiRenderer asciiRenderer = new GameStateAsciiRenderer(registry);

	    ConsoleViewer viewer = new ConsoleViewer(asciiRenderer);

	    GameConfig gameConfig = new GameConfig(
	            new LevelConfigProvider().providesDTO(),
	            new PathsConfigProvider().providesDTO(),
	            new TowersConfigProvider().providesDTO(),
	            new EnemiesConfigProvider().providesDTO());

	    gameEngine.addObserver(viewer);
	    gameEngine.initialize(gameConfig);

	    Scanner scanner = new Scanner(System.in);

	    while (!gameEngine.isGameOver()) {

	        System.out.println();
	        System.out.println("ENTER = tick | command = action | quit = exit");
	        System.out.print("> ");

	        String input = scanner.nextLine().trim();

	        if (input.isEmpty()) {
	            gameEngine.tick();
	        } else if ("quit".equalsIgnoreCase(input)) {
	            break;
	        } else {
	            try {
	                GameCommand command = parseCommand(input);
	                gameEngine.dispatch(command);
	            } catch (IllegalArgumentException e) {
	                System.out.println("Commande invalide : " + e.getMessage());
	            }
	        }
	    }

		System.out.println("\n=== FIN ===");
	}
	
	private GameCommand parseCommand(String input) {
	    String[] tokens = input.split("\\s+");

	    if (tokens.length == 0) {
	        throw new IllegalArgumentException("Commande vide");
	    }

	    String commandName = tokens[0].toLowerCase();

	    return switch (commandName) {
	        case "place-tower" -> parsePlaceTower(tokens);
	        default -> throw new IllegalArgumentException(
	                "Commande inconnue : " + commandName
	        );
	    };
	}
	
	private GameCommand parsePlaceTower(String[] tokens) {
	    if (tokens.length != 5) {
	        throw new IllegalArgumentException(
	            "Usage : place-tower <playerId> <towerType> <x> <y>"
	        );
	    }

	    try {
	        String playerId = tokens[1];
	        String towerType = tokens[2];
	        int x = Integer.parseInt(tokens[3]);
	        int y = Integer.parseInt(tokens[4]);

	        return new PlaceTowerCommand(x, y, towerType, UUID.fromString(playerId));

	    } catch (NumberFormatException e) {
	        throw new IllegalArgumentException(
	            "x et y doivent être des entiers"
	        );
	    }
	}


}
