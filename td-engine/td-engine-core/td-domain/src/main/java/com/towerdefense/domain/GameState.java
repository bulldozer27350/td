package com.towerdefense.domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.towerdefense.domain.dynamik.enemy.Enemy;
import com.towerdefense.domain.dynamik.level.LevelProgress;
import com.towerdefense.domain.dynamik.tower.Tower;
import com.towerdefense.domain.player.PlayerState;
import com.towerdefense.domain.projectile.Projectile;

/**
 * Represents the overall state of the game, including towers, enemies,
 * projectiles, level progress, and player state.
 */
public class GameState {

	private final Map<EntityId, Tower> towers = new HashMap<>();
	private final Map<EntityId, Enemy> enemies = new HashMap<>();
	private final Map<EntityId, Projectile> projectiles = new HashMap<>();
	private LevelProgress levelProgress;
	private StateEnum state;
	private PlayerState player;

	public Collection<Tower> towers() {
		return towers.values();
	}

	public Collection<Enemy> enemies() {
		return enemies.values();
	}

	public Collection<Projectile> projectiles() {
		return projectiles.values();
	}

	public void addTower(Tower t) {
		towers.put(t.id(), t);
	}

	public void removeTower(EntityId id) {
		towers.remove(id);
	}

	public Tower getTower(EntityId id) {
		return towers.get(id);
	}

	public void addEnemy(Enemy e) {
		enemies.put(e.id(), e);
	}

	public void removeEnemy(EntityId id) {
		enemies.remove(id);
	}

	public void addProjectile(Projectile p) {
		projectiles.put(p.id(), p);
	}

	public void removeProjectile(EntityId id) {
		projectiles.remove(id);
	}


	/**
	 * setter for Game State
	 */
	public void setState(StateEnum state) {
		this.state = state;
	}

	/**
	 * getter for Game State
	 * 
	 * @return state of the game
	 */
	public StateEnum getState() {
		return state;
	}

	/** Getter and Setter for Level Progress */
	public LevelProgress levelProgress() {
		return levelProgress;
	}

	public void setLevelProgress(LevelProgress progress) {
		this.levelProgress = progress;
	}

	/** Getter and Setter for Player State */
	public PlayerState player() {
		return player;
	}

	public void setPlayer(PlayerState playerState) {
		this.player = playerState;
	}

	/** Grid dimensions */
	public int gridWidth() {
		return 15;
	}

	public int gridHeight() {
		return 15;
	}

	/** Returns the game object at the specified position, or null if none exists */
	public GameObject objectAt(Position pos) {

		// Towers
		for (Tower t : towers.values()) {
			if (t.position().equals(pos)) {
				return t;
			}
		}

		// Enemies
		for (Enemy e : enemies.values()) {
			int ex = (int) Math.round(e.position().x());
			int ey = (int) Math.round(e.position().y());
			if (pos.x() == ex && pos.y() == ey) {
				return e;
			}
		}

		// Projectiles
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
	/** String representation of the game state */
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getName());
		builder.append(": {towers: {");
		builder.append("\n");
		this.towers.values().stream().forEach(t -> builder.append(t).append(", "));
		builder.append("\n");
		builder.append("}, enemies: {");
		builder.append("\n");
		this.enemies.values().stream().forEach(e -> builder.append(e).append(", "));
		builder.append("\n");
		builder.append("}, projectiles:{");
		builder.append("\n");
		this.projectiles.values().stream().forEach(p -> builder.append(p).append(", "));
		builder.append("\n");
		builder.append("}");
		return builder.toString();
	}
}
