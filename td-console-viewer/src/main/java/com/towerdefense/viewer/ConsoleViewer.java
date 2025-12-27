package com.towerdefense.viewer;

import com.towerdefense.engine.api.GameStateObserver;
import com.towerdefense.engine.api.model.GameStateDTO;
import com.towerdefense.engine.api.model.LevelMapDTO;

public class ConsoleViewer implements GameStateObserver {

    private final GameStateAsciiRenderer renderer;

    public ConsoleViewer(GameStateAsciiRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void onStateUpdated(GameStateDTO state, int tick) {
        System.out.println(renderer.print(state));
    }

	@Override
	public void onGameCreated(LevelMapDTO levelMap) {
		renderer.setMap(levelMap);
	}

	@Override
	public void onGameWon(GameStateDTO state) {
		System.out.println("Partie terminée avec succès");
	}

	@Override
	public void onGameLoose() {
		System.out.println("Partie écouhée");
	}

}
