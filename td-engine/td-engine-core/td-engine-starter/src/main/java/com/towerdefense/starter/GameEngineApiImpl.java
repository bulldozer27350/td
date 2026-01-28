package com.towerdefense.starter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.towerdefense.config.EngineContext;
import com.towerdefense.config.assembler.EnemyFactoryAssembler;
import com.towerdefense.config.assembler.LevelAssembler;
import com.towerdefense.config.assembler.PathAssembler;
import com.towerdefense.config.assembler.TowerTypeAssembler;
import com.towerdefense.config.mapper.EnemyPathMapper;
import com.towerdefense.config.registry.EnemyFactoryRegistry;
import com.towerdefense.config.registry.TowerTypeRegistry;
import com.towerdefense.domain.EntityId;
import com.towerdefense.domain.GameState;
import com.towerdefense.domain.Position;
import com.towerdefense.domain.StateEnum;
import com.towerdefense.domain.dynamik.level.LevelProgress;
import com.towerdefense.domain.map.EnemyPath;
import com.towerdefense.domain.player.PlayerState;
import com.towerdefense.domain.statik.level.LevelScenarioDefinition;
import com.towerdefense.domain.statik.level.TowerCapacityDefinition;
import com.towerdefense.engine.api.GameEngineApi;
import com.towerdefense.engine.api.GameStateObserver;
import com.towerdefense.engine.api.model.GameStateDTO;
import com.towerdefense.engine.api.model.LevelMapDTO;
import com.towerdefense.engine.api.model.MapDimensionsDTO;
import com.towerdefense.engine.api.model.PositionDTO;
import com.towerdefense.engine.api.model.WayDTO;
import com.towerdefense.engine.api.model.command.GameCommand;
import com.towerdefense.engine.api.model.configuration.EnemiesConfig;
import com.towerdefense.engine.api.model.configuration.GameConfig;
import com.towerdefense.engine.api.model.configuration.LevelConfig;
import com.towerdefense.engine.api.model.configuration.PathConfig;
import com.towerdefense.engine.api.model.configuration.TowersConfig;
import com.towerdefense.orchestrator.GameStateMapper;
import com.towerdefense.orchestrator.Sequencer;
import com.towerdefense.orchestrator.runtime.LevelScenario;
import com.towerdefense.orchestrator.runtime.factory.LevelScenarioFactory;
import com.towerdefense.services.GameCommandHandler;

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
    private TowerTypeAssembler towerTypeAssembler;
    private EnemyFactoryAssembler enemyFactoryAssembler;
    private LevelAssembler levelAssembler;
    private List<GameStateObserver> observers;

    private final Map<Class<?>, GameCommandHandler<?>> handlers;

    private int tickNumber;

    public GameEngineApiImpl(LevelScenarioFactory levelScenarioFactory, EngineContext context,
            TowerTypeAssembler towerTypeAssembler, EnemyFactoryAssembler enemyFactoryAssembler,
            LevelAssembler levelAssembler, Map<Class<?>, GameCommandHandler<?>> handlers) {
        this.handlers = handlers;
        this.observers = new ArrayList<>();
        this.playerId = new EntityId(UUID.randomUUID());
        this.state = new GameState();

        this.levelScenarioFactory = levelScenarioFactory;
        this.context = context;

        this.towerTypeAssembler = towerTypeAssembler;
        this.enemyFactoryAssembler = enemyFactoryAssembler;
        this.levelAssembler = levelAssembler;
    }

    public void dispatch(GameCommand command) {
        GameCommandHandler handler = handlers.get(command.getClass());
        handler.handle(command, this.state);
    }

    @Override
    public void initialize(GameConfig gameConfig) {
        this.state = new GameState();
        this.configureLevel(gameConfig);
        this.configureGameMap();
        this.state.setLevelProgress(new LevelProgress(gameConfig.levelConfig().getId(), 0));
        this.state.setState(StateEnum.IN_PROGRESS);
        this.tickNumber = 0;
    }

    private void configureGameMap() {
        MapDimensionsDTO dimensions = new MapDimensionsDTO(this.state.gridWidth(), this.state.gridHeight());

        List<WayDTO> ways = new ArrayList<>();
        this.context.enemyPaths().values().forEach(path -> {
            List<Position> positions = path.getWay();
            List<PositionDTO> positionDTOs = new ArrayList<>();
            positions.forEach(pos -> positionDTOs.add(new PositionDTO(pos.x(), pos.y())));
            ways.add(new WayDTO(positionDTOs));
        });

        LevelMapDTO levelMapDTO = new LevelMapDTO(dimensions, ways);

        this.observers.forEach(observer -> observer.onGameCreated(levelMapDTO));
    }

    @Override
    public void tick() {
        this.sequencer.tick(this.state, this.tickNumber, this.observers);
        this.tickNumber++;
    }

    private void configureLevel(GameConfig gameConfig) {
        this.addTowersConfiguration(gameConfig.towersConfig());
        this.addEnemiesConfiguration(gameConfig.enemiesConfig());
        this.addLevelConfiguration(gameConfig.levelConfig());

        LevelScenario levelScenario = levelScenarioFactory.create(this.context.levelScenarioDefinition(),
                this.context.enemyFactoryRegistry(), this.context.enemyPaths());

        this.state.setPlayer(new PlayerState(this.playerId, gameConfig.levelConfig().getStartingMoney(),
                gameConfig.levelConfig().getStartingLives()));

        Map<String, Integer> towerMaxLevels = levelScenario.getTowerCapacities().stream().collect(
                Collectors.toMap(TowerCapacityDefinition::getTowerTypeId, TowerCapacityDefinition::getMaxLevel));
        this.state.setTowerMaxLevels(towerMaxLevels);

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
        this.addPathsConfiguration(levelConfig.getPaths());
    }

    private void addPathsConfiguration(List<PathConfig> pathsConfig) {
        if (pathsConfig != null && !pathsConfig.isEmpty()) {
            List<EnemyPath> path = pathsConfig.stream().map(EnemyPathMapper::toDomain).toList();
            Map<String, EnemyPath> pathMap = new HashMap<>();
            path.forEach(p -> pathMap.put(p.getIdentifier(), p));
            this.context.setEnemyPaths(pathMap);
        }
    }

    @Override
    public void addObserver(GameStateObserver observer) {
        this.observers.add(observer);
        this.sequencer.addObserver(observer);
    }

    @Override
    public boolean isGameOver() {
        return state.getState() == StateEnum.TERMINATED;
    }

    @Override
    public GameStateDTO getState() {
        return GameStateMapper.toDTO(state);
    }

}
