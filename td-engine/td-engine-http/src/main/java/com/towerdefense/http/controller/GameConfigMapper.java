package com.towerdefense.http.controller;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.towerdefense.engine.api.model.configuration.AttackConfig;
import com.towerdefense.engine.api.model.configuration.EnemiesConfig;
import com.towerdefense.engine.api.model.configuration.EnemyTypeConfig;
import com.towerdefense.engine.api.model.configuration.GameConfig;
import com.towerdefense.engine.api.model.configuration.LevelConfig;
import com.towerdefense.engine.api.model.configuration.PathConfig;
import com.towerdefense.engine.api.model.configuration.PathsConfig;
import com.towerdefense.engine.api.model.configuration.PointConfig;
import com.towerdefense.engine.api.model.configuration.TowerLevelConfig;
import com.towerdefense.engine.api.model.configuration.TowerTypeConfig;
import com.towerdefense.engine.api.model.configuration.TowersConfig;
import com.towerdefense.engine.api.model.configuration.WaveConfig;

@Component
public class GameConfigMapper {

    public GameConfig toBusiness(com.towerdefense.http.model.GameConfig dto) {
        return new GameConfig(
                toBusiness(dto.getLevelConfig()),
                toBusiness(dto.getPathsConfig()),
                toBusiness(dto.getTowersConfig()),
                toBusiness(dto.getEnemiesConfig())
        );
    }

    // ---------- Level ----------

    private LevelConfig toBusiness(com.towerdefense.http.model.LevelConfig dto) {
        LevelConfig config = new LevelConfig();
        config.setId(dto.getId());
        config.setStartingLives(dto.getStartingLives());
        config.setStartingMoney(dto.getStartingMoney());
        config.setAttacks(
                dto.getAttacks().stream()
                        .map(this::toBusiness)
                        .collect(Collectors.toList())
        );
        return config;
    }

    private AttackConfig toBusiness(com.towerdefense.http.model.AttackConfig dto) {
        return new AttackConfig(
                dto.getId(),
                dto.getWaves().stream()
                        .map(this::toBusiness)
                        .collect(Collectors.toList())
        );
    }

    private WaveConfig toBusiness(com.towerdefense.http.model.WaveConfig dto) {
        return new WaveConfig(
                dto.getId(),
                dto.getStartTick(),
                dto.getSpawnInterval(),
                dto.getCount(),
                dto.getEnemyType(),
                dto.getPathId()
        );
    }

    // ---------- Paths ----------

    private PathsConfig toBusiness(com.towerdefense.http.model.PathsConfig dto) {
        PathsConfig config = new PathsConfig();
        config.setPaths(
                dto.getPaths().stream()
                        .map(this::toBusiness)
                        .collect(Collectors.toList())
        );
        return config;
    }

    private PathConfig toBusiness(com.towerdefense.http.model.PathConfig dto) {
        PathConfig config = new PathConfig();
        config.setId(dto.getId());
        config.setPoints(
                dto.getPoints().stream()
                        .map(this::toBusiness)
                        .collect(Collectors.toList())
        );
        return config;
    }

    private PointConfig toBusiness(com.towerdefense.http.model.PointConfig dto) {
        PointConfig point = new PointConfig();
        point.setX(dto.getX());
        point.setY(dto.getY());
        return point;
    }

    // ---------- Enemies ----------

    private EnemiesConfig toBusiness(com.towerdefense.http.model.EnemiesConfig dto) {
        EnemiesConfig config = new EnemiesConfig();
        config.setEnemies(
                dto.getEnemies().stream()
                        .map(this::toBusiness)
                        .collect(Collectors.toList())
        );
        return config;
    }

    private EnemyTypeConfig toBusiness(com.towerdefense.http.model.EnemyTypeConfig dto) {
        EnemyTypeConfig enemy = new EnemyTypeConfig();
        enemy.setId(dto.getId());
        enemy.setHp(dto.getHp());
        enemy.setSpeed(dto.getSpeed());
        enemy.setBounty(dto.getBounty());
        return enemy;
    }

    // ---------- Towers ----------

    private TowersConfig toBusiness(com.towerdefense.http.model.TowersConfig dto) {
        TowersConfig config = new TowersConfig();
        config.setTowers(
                dto.getTowers().stream()
                        .map(this::toBusiness)
                        .collect(Collectors.toList())
        );
        return config;
    }

    private com.towerdefense.engine.api.model.configuration.TowerTypeConfig toBusiness(com.towerdefense.http.model.TowerTypeConfig dto) {
        TowerTypeConfig tower = new TowerTypeConfig();
        tower.setId(dto.getId());
        tower.setName(dto.getName());
        tower.setLevels(
                dto.getLevels().stream()
                        .map(this::toBusiness)
                        .collect(Collectors.toList())
        );
        return tower;
    }

    private TowerLevelConfig toBusiness(com.towerdefense.http.model.TowerLevelConfig dto) {
        return new TowerLevelConfig(
                dto.getLevel(),
                dto.getUpgradeCost(),
                dto.getSellValue(),
                dto.getRange(),
                dto.getDamage(),
                dto.getReloadSeconds(),
                dto.getBuildTimeTicks()
        );
    }
}
