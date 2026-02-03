package com.towerdefense.http.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.towerdefense.engine.api.GameEngineApi;
import com.towerdefense.engine.api.GameRuntime;
import com.towerdefense.engine.api.model.GameStateDTO;
import com.towerdefense.engine.api.model.command.PlaceTowerCommand;
import com.towerdefense.engine.api.model.command.SellTowerCommand;
import com.towerdefense.engine.api.model.command.UpgradeTowerCommand;
import com.towerdefense.http.api.DefaultApi;
import com.towerdefense.http.model.GameConfig;
import com.towerdefense.http.model.GameState;
import com.towerdefense.http.model.GameStatus;
import com.towerdefense.http.model.PlaceTowerRequest;
import com.towerdefense.http.model.SellTowerRequest;
import com.towerdefense.http.model.UpgradeTowerRequest;
import com.towerdefense.starter.GameRuntimeImpl;

import jakarta.validation.Valid;

@RestController
public class GameController implements DefaultApi {

    private GameRuntime gameRuntime;
    private final GameStateMapper mapper;
    private final GameConfigMapper configMapper;
    private final GameEngineApi gameEngineApi;
    
    private GameEventsController gameEventsController;

    public GameController(GameStateMapper mapper, GameConfigMapper configMapper, GameEngineApi engineApi, GameEventsController gameEventsController) {
        this.mapper = mapper;
        this.configMapper = configMapper;
        this.gameEngineApi = engineApi;
        this.gameRuntime = new GameRuntimeImpl(gameEngineApi);
        this.gameEventsController = gameEventsController;
    }

    @Override
    public ResponseEntity<GameState> getGameState() {
        GameStateDTO state = gameRuntime.getState();
        GameState httpState = mapper.toHttpModel(state);
        return ResponseEntity.ok(httpState);
    }

    @Override
    public ResponseEntity<Void> tick() {
        this.gameRuntime.tick();
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> placeTower(PlaceTowerRequest request) {
        // Convertir le UUID String en UUID
        UUID playerId = UUID.fromString(request.getPlayerId());
        
        // Créer la commande avec les bons paramètres dans le bon ordre
        PlaceTowerCommand command = new PlaceTowerCommand(
            request.getX(),
            request.getY(),
            request.getTowerType(),
            playerId
        );
        
        gameRuntime.submit(command);
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<Void> upgradeTower(UpgradeTowerRequest request) {
        UUID playerId = UUID.fromString(request.getPlayerId());
        
        UpgradeTowerCommand command = new UpgradeTowerCommand(
            request.getTowerXPosition(),
            request.getTowerYPosition(),
            playerId
        );
        
        gameRuntime.submit(command);
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<Void> sellTower(SellTowerRequest request) {
        UUID playerId = UUID.fromString(request.getPlayerId());
        
        SellTowerCommand command = new SellTowerCommand(
            request.getTowerXPosition(),
            request.getTowerYPosition(),
            playerId
        );
        
        gameRuntime.submit(command);
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<GameStatus> getGameStatus() {
        GameStatus status = new GameStatus();
        status.setGameOver(gameRuntime.isGameOver());
        return ResponseEntity.ok(status);
    }

	@Override
	public ResponseEntity<Void> initializeGame(@Valid GameConfig gameConfig) {
		this.gameRuntime = new GameRuntimeImpl(gameEngineApi);
		
		// Enregistrer le contrôleur SSE comme observer
	    gameRuntime.addObserver(gameEventsController);
	    com.towerdefense.engine.api.model.configuration.GameConfig config = configMapper.toBusiness(gameConfig);
	    gameRuntime.initialize(config);
	    return ResponseEntity.noContent().build();
	}

}