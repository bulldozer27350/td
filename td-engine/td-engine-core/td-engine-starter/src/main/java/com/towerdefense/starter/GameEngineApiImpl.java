package com.towerdefense.starter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.towerdefense.config.EngineContext;
import com.towerdefense.config.assembler.EnemyFactoryAssembler;
import com.towerdefense.config.assembler.LevelAssembler;
import com.towerdefense.config.assembler.PathAssembler;
import com.towerdefense.config.assembler.TowerTypeAssembler;
import com.towerdefense.config.registry.EnemyFactoryRegistry;
import com.towerdefense.config.registry.TowerTypeRegistry;
import com.towerdefense.domain.EntityId;
import com.towerdefense.domain.GameState;
import com.towerdefense.domain.Position;
import com.towerdefense.domain.StateEnum;
import com.towerdefense.domain.dynamik.tower.Tower;
import com.towerdefense.domain.intentions.BuildTowerIntention;
import com.towerdefense.domain.intentions.UpgradeTowerIntention;
import com.towerdefense.domain.map.EnemyPath;
import com.towerdefense.domain.player.PlayerState;
import com.towerdefense.domain.statik.level.LevelScenarioDefinition;
import com.towerdefense.domain.statik.tower.TowerType;
import com.towerdefense.engine.api.GameEngineApi;
import com.towerdefense.engine.api.GameStateObserver;
import com.towerdefense.engine.api.model.LevelMapDTO;
import com.towerdefense.engine.api.model.MapDimensionsDTO;
import com.towerdefense.engine.api.model.PositionDTO;
import com.towerdefense.engine.api.model.WayDTO;
import com.towerdefense.engine.api.model.configuration.EnemiesConfig;
import com.towerdefense.engine.api.model.configuration.GameConfig;
import com.towerdefense.engine.api.model.configuration.LevelConfig;
import com.towerdefense.engine.api.model.configuration.PathsConfig;
import com.towerdefense.engine.api.model.configuration.TowersConfig;
import com.towerdefense.orchestrator.Sequencer;
import com.towerdefense.orchestrator.runtime.LevelScenario;
import com.towerdefense.orchestrator.runtime.factory.LevelScenarioFactory;
import com.towerdefense.services.TowerServices;

/**
 * Implémentation de l'API du moteur de jeu pour démarrer et gérer les niveaux.
 */
public class GameEngineApiImpl implements GameEngineApi {

	@Autowired
	private Sequencer sequencer;
	private EntityId playerId;
	private GameState state;
	private EngineContext context;
	private LevelScenarioFactory levelScenarioFactory;
	private TowerServices towerServices;
	private EntityId machineGunId;
	private TowerTypeAssembler towerTypeAssembler;
	private EnemyFactoryAssembler enemyFactoryAssembler;
	private LevelAssembler levelAssembler;
	private PathAssembler pathAssembler;
	private List<GameStateObserver> observers;

	public GameEngineApiImpl(LevelScenarioFactory levelScenarioFactory, 
			EngineContext context, 
			TowerServices towerServices, 
			TowerTypeAssembler towerTypeAssembler,
			EnemyFactoryAssembler enemyFactoryAssembler, 
			LevelAssembler levelAssembler,
			PathAssembler pathAssembler) {
		this.observers = new ArrayList<>();
		this.playerId = new EntityId(UUID.randomUUID());
		this.state = new GameState();

		this.levelScenarioFactory = levelScenarioFactory;
		this.context = context;
		this.towerServices = towerServices;

		this.towerTypeAssembler = towerTypeAssembler;
		this.enemyFactoryAssembler = enemyFactoryAssembler;
		this.levelAssembler = levelAssembler;
		this.pathAssembler = pathAssembler;

	}

	/**
	 * Tente d'améliorer la tour mitrailleuse.
	 */
	private void upgradeTower(GameState state) {
		this.towerServices.attemptUpgradeTower(state, new UpgradeTowerIntention(playerId, machineGunId));
	}

	@Override
	public void startLevel(GameConfig gameConfig) {
		this.configureLevel(gameConfig);
		this.configureGameMap();
		this.startLevel();
	}

	private void configureGameMap() {
		MapDimensionsDTO dimensions = new MapDimensionsDTO(
				this.state.gridWidth(),
				this.state.gridHeight());
		
		List<WayDTO> ways = new ArrayList<>();
		this.context.enemyPaths().values().forEach(path -> {
			List<Position> positions = path.getWay();
			List<PositionDTO> positionDTOs = new ArrayList<>();
			positions.forEach(pos -> positionDTOs.add(
					new PositionDTO(pos.x(), pos.y())));
			ways.add(new WayDTO(positionDTOs));
		});
		
		LevelMapDTO levelMapDTO = new LevelMapDTO(dimensions, ways);
		
		this.observers.forEach(observer -> observer.onGameCreated(levelMapDTO));
	}

	private void startLevel() {
		int tickNumber = 0;
		int upgradeTowerNumber = 0;
		this.state.setState(StateEnum.IN_PROGRESS);
		TowerType machinegunType = this.context.towerTypeRegistry().get("machinegun");
		TowerType shotgunType = this.context.towerTypeRegistry().get("shotgun");

		while (this.state.getState() == StateEnum.IN_PROGRESS) {
			this.sequencer.tick(this.state, tickNumber);
			if (tickNumber == 2) {
				boolean built = this.towerServices.attemptBuildTower(state,
						new BuildTowerIntention(this.playerId, new Position(2, 3), machinegunType));
				if (built) {
					this.machineGunId = ((Tower) this.state.objectAt(new Position(2, 3))).id();
				}
			}
			if (tickNumber == 4) {
				this.towerServices.attemptBuildTower(this.state,
						new BuildTowerIntention(this.playerId, new Position(5, 10), shotgunType));
			}
			// à partir du tour 25, on essaie d'améliorer la première tour
			if (upgradeTowerNumber == 0 && tickNumber > 25) {
				upgradeTower(this.state);
				upgradeTowerNumber++;
			}
			try {
				Thread.sleep(300);// 83 ?
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			tickNumber++;
		}
	}

	private void configureLevel(GameConfig gameConfig) {
		this.addPathsConfiguration(gameConfig.pathsConfig());
		this.addTowersConfiguration(gameConfig.towersConfig());
		this.addEnemiesConfiguration(gameConfig.enemiesConfig());
		this.addLevelConfiguration(gameConfig.levelConfig());
		
		LevelScenario levelScenario = levelScenarioFactory.create(
				this.context.levelScenarioDefinition(),
				this.context.enemyFactoryRegistry(), 
				this.context.enemyPaths());
		
		this.state.setPlayer(
				new PlayerState(
						this.playerId, 
						gameConfig.levelConfig().getStartingMoney(),
						gameConfig.levelConfig().getStartingLives()
						)
				);
		this.sequencer.setLevel(levelScenario);
	}

	private void addTowersConfiguration(TowersConfig towersConfig) {
		TowerTypeRegistry registry = towerTypeAssembler.towerTypeRegistry(towersConfig);
		this.context.setTowerTypeRegistry(registry);
	}

	private void addEnemiesConfiguration(EnemiesConfig enemiesConfig) {
		EnemyFactoryRegistry registry = enemyFactoryAssembler.enemyFactoryRegistry(enemiesConfig);
		this.context.setEnemyFactoryRegistry(registry);
	}

	private void addLevelConfiguration(LevelConfig levelConfig) {
		LevelScenarioDefinition scenarioDefinition = this.levelAssembler.levelScenario(levelConfig);
		this.context.setLevelScenarioDefinition(scenarioDefinition);
	}

	private void addPathsConfiguration(PathsConfig pathsConfig) {
		List<EnemyPath> path = this.pathAssembler.enemyPaths(pathsConfig);
		Map<String, EnemyPath> pathMap = new HashMap<>();
		path.forEach(p -> pathMap.put(p.getIdentifier(), p));
		this.context.setEnemyPaths(pathMap);
	}

	@Override
	public void addObserver(GameStateObserver observer) {
		this.observers.add(observer);
		this.sequencer.addObserver(observer);
	}

}
