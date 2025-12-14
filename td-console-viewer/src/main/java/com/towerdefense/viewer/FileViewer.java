package com.towerdefense.viewer;

import com.towerdefense.domain.GameState;
import com.towerdefense.orchestrator.listener.GameStateObserver;

public class FileViewer implements GameStateObserver {

    private final GameStateAsciiRenderer renderer;
	private String outputFile;

    public FileViewer(GameStateAsciiRenderer renderer, String outputFile) {
        this.renderer = renderer;
        this.outputFile = outputFile;
    }

    @Override
    public void onStateUpdated(GameState state, int tick) {
    	String ascii = renderer.print(state);
        AsciiFileWriter.write(outputFile, ascii, tick);
    }
}
