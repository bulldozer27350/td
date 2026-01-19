package com.towerdefense.runner;

import java.io.File;
import java.util.Scanner;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.towerdefense.engine.api.GameRuntime;
import com.towerdefense.engine.api.model.EnemyDTO;
import com.towerdefense.engine.api.model.ProjectileDTO;
import com.towerdefense.engine.api.model.TowerDTO;
import com.towerdefense.engine.api.model.command.GameCommand;
import com.towerdefense.engine.api.model.command.PlaceTowerCommand;
import com.towerdefense.engine.api.model.command.SellTowerCommand;
import com.towerdefense.engine.api.model.command.UpgradeTowerCommand;
import com.towerdefense.engine.api.model.configuration.GameConfig;
import com.towerdefense.runner.config.reader.EnemiesConfigProvider;
import com.towerdefense.runner.config.reader.LevelConfigProvider;
import com.towerdefense.runner.config.reader.TowersConfigProvider;
import com.towerdefense.viewer.ConsoleViewer;
import com.towerdefense.viewer.FileViewer;
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

	private final GameRuntime runtime;

    public ConsoleGameRunner(GameRuntime runtime) {
        this.runtime = runtime;
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
        FileViewer fileViewer = new FileViewer(
                asciiRenderer,
		        String.join(File.separator,System.getProperty("user.dir"),"td-console-viewer","src","test","resources","output.txt"));

        // Observers
        runtime.addObserver(viewer);
        runtime.addObserver(fileViewer);

        // Game configuration
        GameConfig gameConfig = new GameConfig(
                new LevelConfigProvider().providesDTO(),
                new TowersConfigProvider().providesDTO(),
                new EnemiesConfigProvider().providesDTO()
        );

        runtime.initialize(gameConfig);

        Scanner scanner = new Scanner(System.in);

        while (!runtime.isGameOver()) {

            System.out.println();
            System.out.println("ENTER = tick | command = action | quit = exit");
            System.out.print("> ");

            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                runtime.tick();
            } else if ("quit".equalsIgnoreCase(input)) {
                break;
            } else {
                try {
                    GameCommand command = parseCommand(input);
                    runtime.submit(command);
                } catch (IllegalArgumentException e) {
                    System.out.println(
                        "A problem occurs while executing command : " + e.getMessage()
                    );
                }
            }
        }

        System.out.println("\n=== FIN ===");
    }
	
	private GameCommand parseCommand(String input) {
	    String[] tokens = input.split("\\s+");

	    if (tokens.length == 0) {
	        throw new IllegalArgumentException("Empty command");
	    }

	    String commandName = tokens[0].toLowerCase();

	    return switch (commandName) {
	        case "buy" -> parsePlaceTower(tokens);
	        case "upgrade" -> parseUpgradeTower(tokens);
	        case "sell" -> parseSellTower(tokens);
	        default -> throw new IllegalArgumentException(
	                "Unknown command : " + commandName
	        );
	    };
	}
	
	private GameCommand parseUpgradeTower(String[] tokens) {
		if (tokens.length != 4) {
			throw new IllegalArgumentException(
					"Usage : upgrade <playerId> <x> <y>"
					);
		}
		
		try {
			String playerId = tokens[1];
			int x = Integer.parseInt(tokens[2]);
			int y = Integer.parseInt(tokens[3]);
			
			return new UpgradeTowerCommand(x, y, UUID.fromString(playerId));
			
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"x and y should be integer"
					);
		}
	}
	
	private GameCommand parseSellTower(String[] tokens) {
		if (tokens.length != 4) {
			throw new IllegalArgumentException(
					"Usage : sell <playerId> <x> <y>"
					);
		}
		
		try {
			String playerId = tokens[1];
			int x = Integer.parseInt(tokens[2]);
			int y = Integer.parseInt(tokens[3]);
			
			return new SellTowerCommand(x, y, UUID.fromString(playerId));
			
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"x and y should be integer"
					);
		}
	}
	
	private GameCommand parsePlaceTower(String[] tokens) {
	    if (tokens.length != 5) {
	        throw new IllegalArgumentException(
	            "Usage : place <playerId> <towerType> <x> <y>"
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
	            "x and y should be integer"
	        );
	    }
	}


}
