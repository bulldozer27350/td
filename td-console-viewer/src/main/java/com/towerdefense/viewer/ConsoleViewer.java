package com.towerdefense.viewer;

import com.towerdefense.domain.GameState;
import com.towerdefense.orchestrator.listener.GameStateObserver;

public class ConsoleViewer implements GameStateObserver {

    private final GameStateAsciiRenderer renderer;

    public ConsoleViewer(GameStateAsciiRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void onStateUpdated(GameState state, int tick) {
        System.out.println(renderer.print(state));
    }
}
