package com.towerdefense.domain;

import com.towerdefense.domain.enemy.Enemy;
import com.towerdefense.domain.projectile.Projectile;
import com.towerdefense.domain.tower.Tower;

import java.util.*;

public class GameState {

    private final Map<EntityId, Tower> towers = new HashMap<>();
    private final Map<EntityId, Enemy> enemies = new HashMap<>();
    private final Map<EntityId, Projectile> projectiles = new HashMap<>();

    private int playerMoney = 0;

    public Collection<Tower> towers() { return towers.values(); }
    public Collection<Enemy> enemies() { return enemies.values(); }
    public Collection<Projectile> projectiles() { return projectiles.values(); }
    public int playerMoney() { return playerMoney; }

    public void addMoney(int amount) { playerMoney += amount; }
    public void removeMoney(int amount) { playerMoney -= amount; }

    public void addTower(Tower t) { towers.put(t.id(), t); }
    public void removeTower(EntityId id) { towers.remove(id); }

    public void addEnemy(Enemy e) { enemies.put(e.id(), e); }
    public void removeEnemy(EntityId id) { enemies.remove(id); }

    public void addProjectile(Projectile p) { projectiles.put(p.id(), p); }
    public void removeProjectile(EntityId id) { projectiles.remove(id); }
	public int gridWidth() {
		return 15;
	}
	public int gridHeight() {
		return 15;
	}
	
	public GameObject objectAt(Position pos) {

	    // Towers (déjà positionnées à des entiers — pas de changement)
	    for (Tower t : towers.values()) {
	        if (t.position().equals(pos)) {
	            return t;
	        }
	    }

	    // Enemies : positions flottantes → arrondi
	    for (Enemy e : enemies.values()) {
	        int ex = (int) Math.round(e.position().x());
	        int ey = (int) Math.round(e.position().y());
	        if (pos.x() == ex && pos.y() == ey) {
	            return e;
	        }
	    }

	    // Projectiles : positions flottantes → arrondi
	    for (Projectile p : projectiles.values()) {
	        int px = (int) Math.round(p.position().x());
	        int py = (int) Math.round(p.position().y());
	        if (pos.x() == px && pos.y() == py) {
	            return p;
	        }
	    }

	    return null;
	}
	
	 @Override
	    public String toString() {
	    	StringBuilder builder = new StringBuilder();
	    	builder.append(this.getClass().getName());
	    	builder.append(": {towers: {");
	    	builder.append("\n");
	    	towers.values().stream().forEach(t->builder.append(t).append(", "));
	    	builder.append("\n");
	    	builder.append("}, enemies: {");
	    	builder.append("\n");
	    	enemies.values().stream().forEach(e->builder.append(e).append(", "));
	    	builder.append("\n");
	    	builder.append("}, projectiles:{");
	    	builder.append("\n");
	    	projectiles.values().stream().forEach(p->builder.append(p).append(", "));
	    	builder.append("\n");
	    	builder.append("}, playerMoney:");
	    	builder.append(playerMoney);
	    	builder.append("}");
	    	return builder.toString();
	    }
}
