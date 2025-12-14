package com.towerdefense.runner;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.towerdefense.domain.EntityId;
import com.towerdefense.domain.GameState;
import com.towerdefense.domain.Position;
import com.towerdefense.domain.enemy.BasicEnemyFactory;
import com.towerdefense.domain.enemy.Enemy;
import com.towerdefense.domain.enemy.EnemyFactory;
import com.towerdefense.domain.map.EnemyPath;
import com.towerdefense.domain.projectile.Projectile;
import com.towerdefense.domain.tower.Tower;
import com.towerdefense.orchestrator.Sequencer;
import com.towerdefense.orchestrator.spawn.EnemyWave;
import com.towerdefense.viewer.ConsoleViewer;
import com.towerdefense.viewer.FileViewer;
import com.towerdefense.viewer.GameStateAsciiRenderer;
import com.towerdefense.viewer.RendererRegistry;
import com.towerdefense.viewer.renderers.EnemyRenderer;
import com.towerdefense.viewer.renderers.ProjectileRenderer;
import com.towerdefense.viewer.renderers.TowerRenderer;

@Component
public class ConsoleGameRunner implements CommandLineRunner {

	@Autowired
	private Sequencer sequencer;

	@Override
	public void run(String... args) throws Exception {
		System.out.println("=== Tower Defense Console Runner (Spring) ===");

		GameState state = new GameState();

		// Ajouter des tours
		state.addTower(new Tower(EntityId.random(), new Position(2, 3), 3.5, 10, 1.0));

		state.addTower(new Tower(EntityId.random(), new Position(5, 10), 3, 5, 1.0));

		EnemyFactory basicEnemy = new BasicEnemyFactory(
				60, // hp
				1 // speed
		);
		EnemyPath path = new EnemyPath(
				List.of(
						new Position(0, 0) 
						,new Position(5, 0) 
						,new Position(5, 8) 
						,new Position(8, 8)
						,new Position(8, 13)
						,new Position(6, 13)
						,new Position(6, 11)
						,new Position(0, 11)
						));

		EnemyWave wave1 = new EnemyWave(
			    2,   // start tick
			    2,   // interval
			    5,   // count
			    basicEnemy,
			    path
			);

		// Injecter la wave dans le moteur
		this.sequencer.spawner().addWave(wave1);

		// Viewer
		RendererRegistry registry = new RendererRegistry();
		registry.register(Enemy.class, new EnemyRenderer());
		registry.register(Tower.class, new TowerRenderer());
		registry.register(Projectile.class, new ProjectileRenderer());
		GameStateAsciiRenderer asciiRenderer = new GameStateAsciiRenderer(registry);

		ConsoleViewer viewer = new ConsoleViewer(asciiRenderer);
		FileViewer fileviewer = new FileViewer(asciiRenderer,
				"D:\\Depots\\td\\td-console-viewer\\src\\test\\resources\\output.txt");
		sequencer.addObserver(viewer);
		sequencer.addObserver(fileviewer);

		int ticks = 60;

		for (int i = 0; i < ticks; i++) {
			System.out.println("\n--- TICK " + i + " ---");
			sequencer.tick(state, i);
			Thread.sleep(300);
		}

		System.out.println("\n=== FIN ===");
	}
}
