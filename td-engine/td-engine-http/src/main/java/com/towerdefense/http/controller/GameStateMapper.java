package com.towerdefense.http.controller;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.towerdefense.engine.api.model.EnemyDTO;
import com.towerdefense.engine.api.model.GameStateDTO;
import com.towerdefense.engine.api.model.LevelPlayerDTO;
import com.towerdefense.engine.api.model.LevelProgressDTO;
import com.towerdefense.engine.api.model.PositionDTO;
import com.towerdefense.engine.api.model.ProjectileDTO;
import com.towerdefense.engine.api.model.TowerDTO;
import com.towerdefense.http.model.Enemy;
import com.towerdefense.http.model.GameState;
import com.towerdefense.http.model.LevelPlayer;
import com.towerdefense.http.model.LevelProgress;
import com.towerdefense.http.model.Position;
import com.towerdefense.http.model.Projectile;
import com.towerdefense.http.model.Tower;

@Component
public class GameStateMapper {

    public GameState toHttpModel(GameStateDTO dto) {
        GameState state = new GameState();
        
        state.setPlayer(toHttpPlayer(dto.player()));
        state.setTowers(dto.towers().stream()
            .map(this::toHttpTower)
            .collect(Collectors.toList()));
        state.setEnemies(dto.enemies().stream()
            .map(this::toHttpEnemy)
            .collect(Collectors.toList()));
        state.setProjectiles(dto.projectiles().stream()
            .map(this::toHttpProjectile)
            .collect(Collectors.toList()));
        
        return state;
    }

    private LevelPlayer toHttpPlayer(LevelPlayerDTO dto) {
        LevelPlayer player = new LevelPlayer();
        player.setId(dto.id());
        player.setCurrentGold(dto.currentGold());
        player.setCurrentLives(dto.currentLives());
        player.setProgress(toHttpProgress(dto.progress()));
        return player;
    }

    private LevelProgress toHttpProgress(LevelProgressDTO dto) {
        LevelProgress progress = new LevelProgress();
        progress.setLevelIndex(dto.levelIndex());
        progress.setAttackIndex(dto.attackIndex());
        return progress;
    }

    private Tower toHttpTower(TowerDTO dto) {
        Tower tower = new Tower();
        tower.setId(dto.id());
        tower.setPosition(toHttpPosition(dto.position()));
        tower.setTowerType(dto.towerType());  
        tower.setState(dto.state().name());
        return tower;
    }

    private Enemy toHttpEnemy(EnemyDTO dto) {
        Enemy enemy = new Enemy();
        enemy.setId(dto.id());
        enemy.setPosition(toHttpPosition(dto.position()));
        enemy.setMaxHp(dto.maxHp());
        enemy.setCurrentHp(dto.currentHp());
        enemy.setIsAlive(dto.isAlive());
        return enemy;
    }

    private Projectile toHttpProjectile(ProjectileDTO dto) {
        Projectile projectile = new Projectile();
        projectile.setId(dto.id());
        projectile.setPosition(toHttpPosition(dto.position()));
        return projectile;
    }

    private Position toHttpPosition(PositionDTO dto) {
        Position position = new Position();
        position.setX(dto.x());
        position.setY(dto.y());
        return position;
    }
}