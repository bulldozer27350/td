package com.towerdefense.editor.api.model.draft;

public class EditableWave {

	private String id;
    private int startTick;
    private int spawnInterval;
    private int count;
    private String enemyType;
    private String pathId;
    
    public EditableWave() {
	}

    public EditableWave(String id, int startTick, int spawnInterval, int count, String enemyType, String pathId) {
        this.id = id;
    	this.startTick = startTick;
        this.spawnInterval = spawnInterval;
        this.count = count;
        this.enemyType = enemyType;
        this.pathId = pathId;
    }

    public String getId() {
		return id;
	}
    
    public int getStartTick() {
        return startTick;
    }

    public int getSpawnInterval() {
        return spawnInterval;
    }

    public int getCount() {
        return count;
    }

    public String getEnemyType() {
        return enemyType;
    }

    public String getPathId() {
        return pathId;
    }
}
