package com.towerdefense.http.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

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
import com.towerdefense.http.model.UpdateSpeedRequest;
import com.towerdefense.http.model.UpgradeTowerRequest;
import com.towerdefense.http.session.GameSessionManager;

import jakarta.validation.Valid;

@RestController
public class GameController implements DefaultApi {

    private final GameStateMapper mapper;
    private final GameConfigMapper configMapper;
    private final GameSessionManager sessionManager;
    
    @Autowired
    private HttpServletRequest httpRequest;

    public GameController(GameStateMapper mapper, GameConfigMapper configMapper, GameSessionManager sessionManager) {
        this.mapper = mapper;
        this.configMapper = configMapper;
        this.sessionManager = sessionManager;
    }
    
    private String resolveClientId() {
        String clientId = httpRequest.getHeader("X-Client-Id");
        if (clientId == null) {
            clientId = httpRequest.getParameter("clientId");
        }
        return clientId != null ? clientId : "default-session";
    }

    private GameRuntime getGameRuntime() {
        return sessionManager.getOrCreateSession(resolveClientId());
    }

    @Override
    public ResponseEntity<GameState> getGameState() {
        GameStateDTO state = getGameRuntime().getState();
        GameState httpState = mapper.toHttpModel(state);
        return ResponseEntity.ok(httpState);
    }


    @Override
    public ResponseEntity<Void> placeTower(@Valid PlaceTowerRequest request) {
        UUID playerId = UUID.fromString(request.getPlayerId());
        
        PlaceTowerCommand command = new PlaceTowerCommand(
            request.getX(),
            request.getY(),
            request.getTowerType(),
            playerId
        );
        
        getGameRuntime().submit(command);
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<Void> upgradeTower(@Valid UpgradeTowerRequest request) {
        UUID playerId = UUID.fromString(request.getPlayerId());
        
        UpgradeTowerCommand command = new UpgradeTowerCommand(
            request.getTowerXPosition(),
            request.getTowerYPosition(),
            playerId
        );
        
        getGameRuntime().submit(command);
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<Void> sellTower(@Valid SellTowerRequest request) {
        UUID playerId = UUID.fromString(request.getPlayerId());
        
        SellTowerCommand command = new SellTowerCommand(
            request.getTowerXPosition(),
            request.getTowerYPosition(),
            playerId
        );
        
        getGameRuntime().submit(command);
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<GameStatus> getGameStatus() {
        GameStatus status = new GameStatus();
        status.setGameOver(getGameRuntime().isGameOver());
        return ResponseEntity.ok(status);
    }

    @Override
    public ResponseEntity<Void> initializeGame(@Valid GameConfig gameConfig) {
        GameRuntime runtime = getGameRuntime();
        com.towerdefense.engine.api.model.configuration.GameConfig config = configMapper.toBusiness(gameConfig);
        runtime.initialize(config);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> updateSpeed(@Valid UpdateSpeedRequest request) {
        // setSpeed est local à la session du joueur : isolé, pas de globe
        getGameRuntime().setSpeed(request.getSpeedMultiplier());
        return ResponseEntity.accepted().build();
    }

}