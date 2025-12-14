package com.towerdefense.orchestrator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.moe.td.back.tower.TowerBack;
import com.moe.td.back.tower.api.TowerManagement;
import com.towerdefense.domain.EntityId;
import com.towerdefense.domain.GameState;
import com.towerdefense.domain.enemy.Enemy;
import com.towerdefense.domain.intentions.PlaceTowerIntention;
import com.towerdefense.domain.intentions.ShootIntention;
import com.towerdefense.domain.projectile.Projectile;
import com.towerdefense.domain.tower.Tower;
import com.towerdefense.orchestrator.listener.GameStateObserver;
import com.towerdefense.orchestrator.spawn.EnemySpawner;

@Component
public class Sequencer {

	private final TowerManagement towerBack = new TowerBack();
	
	private final EnemySpawner spawner = new EnemySpawner();

	private final List<GameStateObserver> observers = new ArrayList<>();

	public void addObserver(GameStateObserver obs) {
		this.observers.add(obs);
	}
	
	public EnemySpawner spawner() {
        return spawner;
    }

	public void tick(GameState state, int tick) {

		// 0. Spawn ennemis si nécessaire
        this.spawner.tick(state, tick);
		
		// 1. Tick cooldowns
		state.towers().forEach(t -> t.tick());

		// 2. Move enemies
		state.enemies().forEach(e -> e.tick());

		// 3. Towers automatically shoot enemies in range
		for (Tower tower : state.towers()) {
		    if (!tower.isReady()) continue;

		    // Cherche un ennemi à portée
		    Enemy target = state.enemies().stream()
		            .filter(e -> tower.canShootTarget(e.position()))
		            .findFirst()
		            .orElse(null);

		    if (target == null) continue;

		    // Déclenche le tir
		    tower.triggerShot();

		    Projectile p = new Projectile(
		            EntityId.random(),
		            tower.position(),
		            8.0,                     // speed
		            tower.damage(),
		            target.id());

		    state.addProjectile(p);
		}
		
		// 4. Move projectiles
		List<EntityId> hitProjectiles = new ArrayList<>();

		for (Projectile projectile : state.projectiles()) {
			Enemy target = state.enemies().stream().filter(enemy -> enemy.id().equals(projectile.targetId()))
					.findFirst().orElse(null);
			// if no target, launch a new loop cycle
			if (target == null)
				continue;

			projectile.updateTowards(target.position());

			if (projectile.position().equals(target.position())) {
				target.health().applyDamage(projectile.damage());
				hitProjectiles.add(projectile.id());
			}
		}

		hitProjectiles.forEach(state::removeProjectile);
		state.enemies().removeIf(e -> e.health().isDead());

		this.observers.forEach(o -> o.onStateUpdated(state, tick));
	}

	public boolean attemptPlaceTower(GameState state, PlaceTowerIntention intent) {
		if (!towerBack.canPlaceTower(state, intent.position()))
			return false;

		Tower t = new Tower(EntityId.random(), intent.position(), 3.5, 10, 1.0);
		state.addTower(t);
		return true;
	}

	public boolean attemptShoot(GameState state, ShootIntention intent) {
		if (!this.towerBack.canShoot(state, intent))
			return false;

		Tower tower = state.towers().stream().filter(t -> t.id().equals(intent.towerId())).findFirst().orElseThrow();

		Enemy enemy = state.enemies().stream().filter(e -> e.id().equals(intent.targetId())).findFirst().orElseThrow();

		tower.triggerShot();

		Projectile p = new Projectile(EntityId.random(), tower.position(), 8.0, tower.damage(), enemy.id());

		state.addProjectile(p);

		return true;
	}
}
