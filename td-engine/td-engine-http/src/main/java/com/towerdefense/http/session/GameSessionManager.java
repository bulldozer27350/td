package com.towerdefense.http.session;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Component;

import com.towerdefense.engine.api.GameEngineApi;
import com.towerdefense.engine.api.GameRuntime;
import com.towerdefense.http.controller.GameEventsController;
import com.towerdefense.starter.GameRuntimeImpl;

@Component
public class GameSessionManager {
    private final ConcurrentMap<String, GameRuntime> sessions = new ConcurrentHashMap<>();
    private final GameEngineApi gameEngineApi;
    private final GameEventsController gameEventsController;

    public GameSessionManager(GameEngineApi gameEngineApi, GameEventsController gameEventsController) {
        this.gameEngineApi = gameEngineApi;
        this.gameEventsController = gameEventsController;
    }

    public GameRuntime getOrCreateSession(String clientId) {
        return sessions.computeIfAbsent(clientId, id -> {
            GameRuntime runtime = new GameRuntimeImpl(gameEngineApi);
            // On associe l'observateur au runtime (Note: GameEventsController devra filtrer par id)
            runtime.addObserver(gameEventsController.forClient(id));
            return runtime;
        });
    }

    public GameRuntime getSession(String clientId) {
        return sessions.get(clientId);
    }
    
    public void removeSession(String clientId) {
        sessions.remove(clientId);
    }
}
