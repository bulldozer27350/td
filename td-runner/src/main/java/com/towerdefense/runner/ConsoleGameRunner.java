package com.towerdefense.runner;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.towerdefense.domain.EntityId;
import com.towerdefense.domain.GameState;
import com.towerdefense.domain.Position;
import com.towerdefense.domain.dynamik.enemy.Enemy;
import com.towerdefense.domain.dynamik.tower.Tower;
import com.towerdefense.domain.map.EnemyPath;
import com.towerdefense.domain.player.PlayerId;
import com.towerdefense.domain.player.PlayerState;
import com.towerdefense.domain.projectile.Projectile;
import com.towerdefense.domain.statik.enemy.BasicEnemyFactory;
import com.towerdefense.domain.statik.enemy.EnemyFactory;
import com.towerdefense.domain.statik.tower.TowerLevelDefinition;
import com.towerdefense.domain.statik.tower.TowerType;
import com.towerdefense.orchestrator.Sequencer;
import com.towerdefense.orchestrator.runtime.Attack;
import com.towerdefense.orchestrator.runtime.EnemyWave;
import com.towerdefense.orchestrator.runtime.LevelScenario;
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
		state.setPlayer(new PlayerState(new PlayerId(UUID.randomUUID()), 100, 10));

		// Ajouter des tours
		TowerLevelDefinition machineGunDefinition0 = new TowerLevelDefinition(
				0, // tower level
				100, //cost to obtain level
				75, // earned gold when sell
				3.5, //range
				10, // damage
				1.0, // reload time
				2 // build time
				);
		TowerLevelDefinition machineGunDefinition1 = new TowerLevelDefinition(
				1, // tower level
				100, //cost to obtain level
				150, // earned gold when sell
				5.5, //range
				15, // damage
				0.9, // reload time
				3 // build time
				);
		TowerLevelDefinition machineGunDefinition2 = new TowerLevelDefinition(
				2, // tower level
				100, //cost to obtain level
				225, // earned gold when sell
				7.5, //range
				20, // damage
				0.8, // reload time
				5 // build time
				);
		
		TowerLevelDefinition shotgunDefinition0 = new TowerLevelDefinition(
				0, // tower level
				200, //cost to obtain level
				150, // earned gold when sell
				2, //range
				8, // damage
				2.5, // reload time
				2 // build time
				);
		TowerLevelDefinition shotgunDefinition1 = new TowerLevelDefinition(
				1, // tower level
				200, //cost to obtain level
				300, // earned gold when sell
				3.0, //range
				10, // damage
				2.4, // reload time
				2 // build time
				);
		TowerLevelDefinition shotgunDefinition2 = new TowerLevelDefinition(
				2, // tower level
				200, //cost to obtain level
				450, // earned gold when sell
				4.0, //range
				12, // damage
				2.3, // reload time
				2 // build time
				);
		TowerType machineGunType = new TowerType("Machine gun", List.of(machineGunDefinition0, machineGunDefinition1, machineGunDefinition2));
		TowerType shotgunType = new TowerType("Shotgun", List.of(shotgunDefinition0, shotgunDefinition1, shotgunDefinition2));
		
		state.addTower(new Tower(EntityId.random(), new Position(2, 3), machineGunType));

		state.addTower(new Tower(EntityId.random(), new Position(5, 10), shotgunType));

		EnemyFactory fastFactory = new BasicEnemyFactory(
				40, // hp
				1, // speed
				5 // bounty
		);
		
		EnemyFactory tankFactory = new BasicEnemyFactory(
				80, // hp
				0.1, // speed
				20 // bounty
				);
		
		EnemyFactory bossFactory = new BasicEnemyFactory(
				200, // hp
				0.05, // speed
				50 // bounty
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

		EnemyWave w1 = new EnemyWave(0, 0, 2, 5, fastFactory, path);
		EnemyWave w2 = new EnemyWave(1, 3, 3, 3, tankFactory, path);

		Attack attack1 = new Attack(List.of(w1, w2));

		EnemyWave bossWave = new EnemyWave(0, 0, 1, 1, bossFactory, path);
		Attack attack2 = new Attack(List.of(bossWave));
		
		LevelScenario level = new LevelScenario(0, List.of(attack1, attack2));
		sequencer.setLevel(level);
		
		// Viewer
		RendererRegistry registry = new RendererRegistry();
		registry.register(Enemy.class, new EnemyRenderer());
		registry.register(Tower.class, new TowerRenderer());
		registry.register(Projectile.class, new ProjectileRenderer());
		GameStateAsciiRenderer asciiRenderer = new GameStateAsciiRenderer(registry);

		ConsoleViewer viewer = new ConsoleViewer(asciiRenderer);
		FileViewer fileviewer = new FileViewer(asciiRenderer,
				"D:\\Depots\\tower_defense\\td-console-viewer\\src\\test\\resources\\output.txt");
		sequencer.addObserver(viewer);
		sequencer.addObserver(fileviewer);

		int ticks = 100;

		for (int i = 0; i < ticks; i++) {
			System.out.println("\n--- TICK " + i + " ---");
			this.sequencer.tick(state, i);
			Thread.sleep(300);
		}

		System.out.println("\n=== FIN ===");
	}
}
