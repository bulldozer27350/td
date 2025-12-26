package com.towerdefense.runner;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.towerdefense.config.registry.EnemyFactoryRegistry;
import com.towerdefense.config.registry.TowerTypeRegistry;
import com.towerdefense.config.spring.LevelConfiguration;
import com.towerdefense.config.spring.PathConfiguration;
import com.towerdefense.domain.EntityId;
import com.towerdefense.domain.GameState;
import com.towerdefense.domain.Position;
import com.towerdefense.domain.StateEnum;
import com.towerdefense.domain.dynamik.enemy.Enemy;
import com.towerdefense.domain.dynamik.level.LevelScenarioDefinition;
import com.towerdefense.domain.dynamik.tower.Tower;
import com.towerdefense.domain.intentions.BuildTowerIntention;
import com.towerdefense.domain.intentions.UpgradeTowerIntention;
import com.towerdefense.domain.map.EnemyPath;
import com.towerdefense.domain.player.PlayerState;
import com.towerdefense.domain.projectile.Projectile;
import com.towerdefense.domain.statik.tower.TowerType;
import com.towerdefense.orchestrator.Sequencer;
import com.towerdefense.orchestrator.runtime.LevelScenario;
import com.towerdefense.orchestrator.runtime.factory.LevelScenarioFactory;
import com.towerdefense.services.TowerServices;
import com.towerdefense.services.internal.impl.TowerFactory;
import com.towerdefense.viewer.ConsoleViewer;
import com.towerdefense.viewer.FileViewer;
import com.towerdefense.viewer.GameStateAsciiRenderer;
import com.towerdefense.viewer.RendererRegistry;
import com.towerdefense.viewer.renderers.EnemyRenderer;
import com.towerdefense.viewer.renderers.ProjectileRenderer;
import com.towerdefense.viewer.renderers.TowerRenderer;

@Component
/**
 * Classe responsable de l'exécution d'une partie de Tower Defense en mode console.
 */
public class ConsoleGameRunner implements CommandLineRunner {

	@Autowired
	private Sequencer sequencer;

	@Autowired
	private TowerServices towerServices;

	private final TowerTypeRegistry towerTypeRegistry;
	private final Map<String, EnemyPath> enemyPaths;
	private final LevelConfiguration levelConfiguration;
	private final EnemyFactoryRegistry enemyFactoryRegistry;
	private final LevelScenarioFactory levelScenarioFactory;

	/**
	 * Constructeur de ConsoleGameRunner.
	 * @param towerTypeRegistry le registre des types de tours
	 * @param levelConfiguration la configuration du niveau
	 * @param enemyFactoryRegistry le registre des usines d'ennemis
	 * @param levelScenarioFactory la fabrique de scénarios de niveau
	 * @param paths la configuration des chemins
	 */
	public ConsoleGameRunner(TowerTypeRegistry towerTypeRegistry, LevelConfiguration levelConfiguration,
			EnemyFactoryRegistry enemyFactoryRegistry, LevelScenarioFactory levelScenarioFactory,
			PathConfiguration paths) {
		this.towerTypeRegistry = towerTypeRegistry;
		this.levelConfiguration = levelConfiguration;
		this.enemyFactoryRegistry = enemyFactoryRegistry;
		this.levelScenarioFactory = levelScenarioFactory;
		this.enemyPaths = new HashMap<String, EnemyPath>();
		paths.enemyPaths().forEach(path -> this.enemyPaths.put(path.getIdentifier(), path));
	}

	TowerFactory towerFactory = new TowerFactory();

	private EntityId machineGunId = EntityId.random();
	private EntityId playerId = EntityId.random();

	@Override
	/**
	 * Lance une partie de Tower Defense en mode console.
	 */
	public void run(String... args) throws Exception {
		System.out.println("=== Tower Defense Console Runner (Spring) ===");

		// Game State
		GameState state = new GameState();

		this.initializeGameState(state, this.sequencer);

		// Viewer
		RendererRegistry registry = new RendererRegistry();
		registry.register(Enemy.class, new EnemyRenderer());
		registry.register(Tower.class, new TowerRenderer());
		registry.register(Projectile.class, new ProjectileRenderer());
		GameStateAsciiRenderer asciiRenderer = new GameStateAsciiRenderer(registry);

		ConsoleViewer viewer = new ConsoleViewer(asciiRenderer);
		FileViewer fileviewer = new FileViewer(asciiRenderer,
				"D:\\Depots\\tower_defense\\td-console-viewer\\src\\test\\resources\\output.txt");
		this.sequencer.addObserver(viewer);
		this.sequencer.addObserver(fileviewer);

		int i = 0;
		int upgradeTowerNumber = 0;
		state.setState(StateEnum.IN_PROGRESS);
		TowerType machinegunType = towerTypeRegistry.get("machinegun");
		TowerType shotgunType = towerTypeRegistry.get("shotgun");

		while (state.getState() == StateEnum.IN_PROGRESS) {
			System.out.println("\n--- TICK " + i + " ---");
			this.sequencer.tick(state, i);
			if (i == 2) {
				boolean built = this.towerServices.attemptBuildTower(state,
						new BuildTowerIntention(playerId, new Position(2, 3), machinegunType));
				if (built) {
					this.machineGunId = ((Tower) state.objectAt(new Position(2, 3))).id();
				}
			}
			if (i == 4) {
				this.towerServices.attemptBuildTower(state,
						new BuildTowerIntention(playerId, new Position(5, 10), shotgunType));
			}
			// à partir du tour 25, on essaie d'améliorer la première tour
			if (upgradeTowerNumber == 0 && i > 25) {
				upgradeTower(state);
				upgradeTowerNumber++;
			}
			Thread.sleep(300);
			i++;
		}

		System.out.println("\n=== FIN ===");
	}

	/**
	 * Tente d'améliorer la tour mitrailleuse.
	 */
	private void upgradeTower(GameState state) {
		this.towerServices.attemptUpgradeTower(state, new UpgradeTowerIntention(playerId, machineGunId));
	}

	/**
	 * Initialise l'état du jeu avec le joueur et le niveau.
	 */
	private void initializeGameState(GameState state, Sequencer sequencerParam) {
		state.setPlayer(new PlayerState(playerId, 400, 10));

		LevelScenarioDefinition levelDefinition = levelConfiguration.levelScenario();

		LevelScenario levelScenario = levelScenarioFactory.create(levelDefinition, enemyFactoryRegistry, enemyPaths);
		sequencerParam.setLevel(levelScenario);
	}

}
