package com.towerdefense.http.session;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Component;

import com.towerdefense.engine.api.GameEngineApi;
import com.towerdefense.engine.api.GameRuntime;
import com.towerdefense.http.controller.GameEventsController;
import com.towerdefense.starter.GameRuntimeImpl;

@Component
public class GameSessionManager {
    private static final Logger log = LoggerFactory.getLogger(GameSessionManager.class);
    private final ConcurrentMap<String, GameRuntime> sessions = new ConcurrentHashMap<>();
    private final ObjectFactory<GameEngineApi> gameEngineApiFactory;
    private final GameEventsController gameEventsController;

    public GameSessionManager(ObjectFactory<GameEngineApi> gameEngineApiFactory, GameEventsController gameEventsController) {
        this.gameEngineApiFactory = gameEngineApiFactory;
        this.gameEventsController = gameEventsController;
    }

    public GameRuntime getOrCreateSession(String clientId) {
        return sessions.computeIfAbsent(clientId, id -> {
            log.info("Creating new Game Session for clientId={}", id);
            GameEngineApi gameEngineApi = gameEngineApiFactory.getObject();
            GameRuntime runtime = new GameRuntimeImpl(gameEngineApi);
            // On associe l'observateur au runtime
            runtime.addObserver(gameEventsController.forClient(id, this));
            return runtime;
        });
    }


    public GameRuntime getSession(String clientId) {
        return sessions.get(clientId);
    }
    
    public void removeSession(String clientId) {
        GameRuntime runtime = sessions.remove(clientId);
        if (runtime instanceof GameRuntimeImpl) {
            log.info("Destroying Game Session for clientId={}", clientId);
            ((GameRuntimeImpl) runtime).destroy();
        }
    }
}
