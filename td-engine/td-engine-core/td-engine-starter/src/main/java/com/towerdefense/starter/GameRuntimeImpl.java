package com.towerdefense.starter;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.towerdefense.engine.api.GameEngineApi;
import com.towerdefense.engine.api.GameRuntime;
import com.towerdefense.engine.api.GameStateObserver;
import com.towerdefense.engine.api.model.GameStateDTO;
import com.towerdefense.engine.api.model.command.GameCommand;
import com.towerdefense.engine.api.model.configuration.GameConfig;

public class GameRuntimeImpl implements GameRuntime {

    private final GameEngineApi engine;
    private final Queue<GameCommand> queue = new ConcurrentLinkedQueue<>();
    private ScheduledExecutorService scheduler;
    private double currentSpeedMultiplier = 1.0;

    public GameRuntimeImpl(GameEngineApi engine) {
        this.engine = engine;
    }

    @Override
    public void initialize(GameConfig config) {
        engine.initialize(config);
        
        // Démarrage de la boucle de jeu asynchrone côté serveur (Autonomie Backend)
        setSpeed(this.currentSpeedMultiplier);
    }

    @Override
    public synchronized void setSpeed(double speedMultiplier) {
        this.currentSpeedMultiplier = speedMultiplier;
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }
        
        if (speedMultiplier > 0.01) { // 0 = Pause
            long delay = (long) (200 / speedMultiplier);
            scheduler = Executors.newSingleThreadScheduledExecutor();
            // Start tick recursively or via fixed rate
            scheduler.scheduleAtFixedRate(this::tick, delay, delay, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void submit(GameCommand command) {
        queue.add(command);
    }

    @Override
    public void tick() {
        if (engine.isGameOver()) {
            // Arrêter la boucle si le jeu est fini
            if (scheduler != null && !scheduler.isShutdown()) {
                scheduler.shutdown();
            }
            return;
        }

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

    /**
     * Termine explicitement ce runtime pour libérer les ressources RAM et le Scheduler.
     */
    public void destroy() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }
        queue.clear();
    }
}
