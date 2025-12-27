package com.towerdefense.viewer;

import com.towerdefense.engine.api.GameStateObserver;
import com.towerdefense.engine.api.model.GameStateDTO;
import com.towerdefense.engine.api.model.LevelMapDTO;

public class FileViewer implements GameStateObserver {

    private final GameStateAsciiRenderer renderer;
	private String outputFile;

    public FileViewer(GameStateAsciiRenderer renderer, String outputFile) {
        this.renderer = renderer;
        this.outputFile = outputFile;
    }

    @Override
    public void onStateUpdated(GameStateDTO state, int tick) {
    	String ascii = renderer.print(state);
        AsciiFileWriter.write(outputFile, ascii, tick);
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
