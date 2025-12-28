package com.towerdefense.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.towerdefense.engine.api.GameEngineApi;
import com.towerdefense.engine.api.model.EnemyDTO;
import com.towerdefense.engine.api.model.ProjectileDTO;
import com.towerdefense.engine.api.model.TowerDTO;
import com.towerdefense.engine.api.model.configuration.GameConfig;
import com.towerdefense.runner.config.reader.EnemiesConfigProvider;
import com.towerdefense.runner.config.reader.LevelConfigProvider;
import com.towerdefense.runner.config.reader.PathsConfigProvider;
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

	private GameEngineApi gameEngine;

	public ConsoleGameRunner(GameEngineApi gameEngineApi) {
		this.gameEngine = gameEngineApi;
	}

	@Override
	/**
	 * Lance une partie de Tower Defense en mode console.
	 */
	public void run(String... args) throws Exception {
		System.out.println("=== Tower Defense Console Runner (Spring) ===");

		// Viewer
		RendererRegistry registry = new RendererRegistry();
		registry.register(EnemyDTO.class, new EnemyRenderer());
		registry.register(TowerDTO.class, new TowerRenderer());
		registry.register(ProjectileDTO.class, new ProjectileRenderer());
		GameStateAsciiRenderer asciiRenderer = new GameStateAsciiRenderer(registry);

		ConsoleViewer viewer = new ConsoleViewer(asciiRenderer);
		FileViewer fileviewer = new FileViewer(asciiRenderer,
				"D:\\Depots\\tower_defense\\td-console-viewer\\src\\test\\resources\\output.txt");

		GameConfig gameConfig = new GameConfig(
				new LevelConfigProvider().providesDTO(),
				new PathsConfigProvider().providesDTO(), 
				new TowersConfigProvider().providesDTO(),
				new EnemiesConfigProvider().providesDTO());
		this.gameEngine.addObserver(viewer);
		this.gameEngine.addObserver(fileviewer);
		
		this.gameEngine.startLevel(gameConfig);

		System.out.println("\n=== FIN ===");
	}

}
