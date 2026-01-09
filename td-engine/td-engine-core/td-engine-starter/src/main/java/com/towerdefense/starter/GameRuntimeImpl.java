package com.towerdefense.starter;

import java.util.ArrayDeque;
import java.util.Queue;

import com.towerdefense.engine.api.GameEngineApi;
import com.towerdefense.engine.api.GameRuntime;
import com.towerdefense.engine.api.GameStateObserver;
import com.towerdefense.engine.api.model.GameStateDTO;
import com.towerdefense.engine.api.model.command.GameCommand;
import com.towerdefense.engine.api.model.configuration.GameConfig;

public class GameRuntimeImpl implements GameRuntime {

    private final GameEngineApi engine;
    private final Queue<GameCommand> queue = new ArrayDeque<>();

    public GameRuntimeImpl(GameEngineApi engine) {
        this.engine = engine;
    }

    @Override
    public void initialize(GameConfig config) {
        engine.initialize(config);
    }

    @Override
    public void submit(GameCommand command) {
        queue.add(command);
    }

    @Override
    public void tick() {
        while (!queue.isEmpty()) {
            engine.dispatch(queue.poll());
        }
        engine.tick();
    }

    @Override
    public GameStateDTO getState() {
        return engine.getState();
    }

    @Override
    public boolean isGameOver() {
        return engine.isGameOver();
    }

    @Override
    public void addObserver(GameStateObserver observer) {
        engine.addObserver(observer);
    }
}
