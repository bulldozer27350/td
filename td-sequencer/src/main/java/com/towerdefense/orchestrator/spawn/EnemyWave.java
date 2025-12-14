package com.towerdefense.orchestrator.spawn;

import java.util.ArrayList;
import java.util.List;

import com.towerdefense.domain.enemy.Enemy;
import com.towerdefense.domain.enemy.EnemyFactory;
import com.towerdefense.domain.map.EnemyPath;

public class EnemyWave {

    private final int startTick;
    private final int interval;
    private final int count;
    
    private final EnemyFactory factory;
    private final EnemyPath path;
    
    private int spawned = 0;

    public EnemyWave(int startTick, int interval, int count, EnemyFactory factory, EnemyPath path) {
    	this.startTick = startTick;
        this.interval = interval;
        this.count = count;
        this.factory = factory;
        this.path = path;
    }

    public List<Enemy> dueSpawns(int currentTick) {
        List<Enemy> result = new ArrayList<>();

        if (currentTick < startTick) return result;
        if (spawned >= count) return result;
        
        int ticksSinceStart = currentTick - startTick;

        if (ticksSinceStart % interval == 0) {
            Enemy enemy = factory.create(path);
            result.add(enemy);
            spawned++;
        }

        return result;
    }

    public boolean isFinished() {
        return spawned >= count;
    }
}
