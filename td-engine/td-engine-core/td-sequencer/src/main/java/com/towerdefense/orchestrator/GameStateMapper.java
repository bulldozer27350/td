package com.towerdefense.orchestrator;

import java.util.ArrayList;
import java.util.List;

import com.towerdefense.domain.GameState;
import com.towerdefense.domain.dynamik.enemy.Enemy;
import com.towerdefense.domain.dynamik.level.LevelProgress;
import com.towerdefense.domain.dynamik.tower.Tower;
import com.towerdefense.domain.player.PlayerState;
import com.towerdefense.domain.projectile.Projectile;
import com.towerdefense.engine.api.model.EnemyDTO;
import com.towerdefense.engine.api.model.GameStateDTO;
import com.towerdefense.engine.api.model.LevelPlayerDTO;
import com.towerdefense.engine.api.model.LevelProgressDTO;
import com.towerdefense.engine.api.model.PositionDTO;
import com.towerdefense.engine.api.model.ProjectileDTO;
import com.towerdefense.engine.api.model.TowerDTO;

public class GameStateMapper {

	public static GameStateDTO toDTO(GameState state) {
		List<EnemyDTO> enemies = new ArrayList<EnemyDTO>();
		for (Enemy enemy : state.enemies()) {
			enemies.add(GameStateMapper.toDTO(enemy));
		}

		List<TowerDTO> towers = new ArrayList<TowerDTO>();
		for (Tower tower : state.towers()) {
			towers.add(GameStateMapper.toDTO(tower));
		}

		List<ProjectileDTO> projectiles = new ArrayList<ProjectileDTO>();
		for (Projectile projectile : state.projectiles()) {
			projectiles.add(GameStateMapper.toDTO(projectile));
		}

		LevelPlayerDTO levelPlayer = GameStateMapper.toDTO(state.player(), state.levelProgress());

		return new GameStateDTO(levelPlayer, towers, enemies, projectiles);
	}

	private static ProjectileDTO toDTO(Projectile projectile) {
		return new ProjectileDTO(projectile.id().value().toString(),
				new PositionDTO(projectile.position().x(), projectile.position().y()));
	}

	private static EnemyDTO toDTO(Enemy enemy) {
		return new EnemyDTO(enemy.id().value().toString(), new PositionDTO(enemy.position().x(), enemy.position().y()),
				enemy.health().max(), enemy.health().current(), !(enemy.isAtEnd() && enemy.health().current() > 0));
	}

	private static TowerDTO toDTO(Tower tower) {
		return new TowerDTO(tower.id().value().toString(), new PositionDTO(tower.position().x(), tower.position().y()), null,
				null);
	}

	private static LevelPlayerDTO toDTO(PlayerState player, LevelProgress levelProgress) {
		LevelProgressDTO progressDTO;
		
		if (levelProgress == null) {
			// Par défaut, on crée un progress avec des valeurs initiales
			progressDTO = new LevelProgressDTO("unknown", 0);
		} else {
			progressDTO = new LevelProgressDTO(
				levelProgress.levelIndex(), 
				levelProgress.attackIndex()
			);
		}
		
		return new LevelPlayerDTO(
			player.id().value().toString(), 
			player.gold(), 
			player.lives(),
			progressDTO
		);
	}
}