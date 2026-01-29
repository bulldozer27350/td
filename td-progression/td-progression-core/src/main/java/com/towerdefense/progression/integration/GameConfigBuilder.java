package com.towerdefense.progression.integration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.towerdefense.progression.domain.game.GameConfig;
import com.towerdefense.progression.model.Attack;
import com.towerdefense.progression.model.EnemyTypeData;
import com.towerdefense.progression.model.LevelData;
import com.towerdefense.progression.model.LevelPath;
import com.towerdefense.progression.model.TowerCapacity;
import com.towerdefense.progression.model.TowerTypeData;
import com.towerdefense.progression.model.Wave;

//Construit le GameConfig au format attendu par td-engine
public class GameConfigBuilder {
 
 public GameConfig buildGameConfig(
         LevelData level,
         List<TowerTypeData> towers,
         List<EnemyTypeData> enemies) {
     
     return new GameConfig(level, towers, enemies);
 }
 
 private Map<String, Object> buildLevelConfig(LevelData level) {
     Map<String, Object> config = new HashMap<>();
     config.put("id", level.id());
     config.put("startingLives", level.startingLives());
     config.put("startingMoney", level.startingMoney());
     config.put("attacks", buildAttacks(level.attacks()));
     config.put("towerCapacities", buildTowerCapacities(level.towerCapacities()));
     config.put("map", buildMap(level.map()));
     config.put("paths", buildPathsConfig(level.paths()));
     return config;
 }
 
 private Map<String, Object> buildMap(com.towerdefense.progression.model.LevelMap map) {
     Map<String, Object> mapDimensions = new HashMap<String, Object>();
     mapDimensions.put("width", map.width());
     mapDimensions.put("height", map.height());
     return mapDimensions;
}

 private List<Map<String, Object>> buildAttacks(List<Attack> attacks) {
     return attacks.stream().map(attack -> {
         Map<String, Object> attackMap = new HashMap<>();
         attackMap.put("id", attack.id());
         attackMap.put("waves", buildWaves(attack.waves()));
         return attackMap;
     }).toList();
 }
 
 private List<Map<String, Object>> buildWaves(List<Wave> waves) {
     return waves.stream().map(wave -> {
         Map<String, Object> waveMap = new HashMap<>();
         waveMap.put("id", wave.id());
         waveMap.put("startTick", wave.startTick());
         waveMap.put("spawnInterval", wave.spawnInterval());
         waveMap.put("count", wave.count());
         waveMap.put("enemyType", wave.enemyType());
         waveMap.put("pathId", wave.pathId());
         return waveMap;
     }).toList();
 }
 
 private List<Map<String, Object>> buildTowerCapacities(List<TowerCapacity> capacities) {
     return capacities.stream().map(cap -> {
         Map<String, Object> capMap = new HashMap<>();
         capMap.put("towerTypeId", cap.towerTypeId());
         capMap.put("maxRank", cap.maxRank());
         return capMap;
     }).toList();
 }
 
 private Map<String, Object> buildPathsConfig(List<LevelPath> paths) {
     Map<String, Object> config = new HashMap<>();
     for (LevelPath p : paths) {
         config.put("id", p.id());
         config.put("points", p.points().stream().map(point -> {
             Map<String, Object> pointMap = new HashMap<>();
             pointMap.put("x", point.x());
             pointMap.put("y", point.y());
             return pointMap;
         }).toList());
     }
     return config;
 }
 
 private Map<String, Object> buildTowersConfig(List<TowerTypeData> towers) {
     Map<String, Object> config = new HashMap<>();
     config.put("towers", towers.stream().map(tower -> {
         Map<String, Object> towerMap = new HashMap<>();
         towerMap.put("id", tower.id());
         towerMap.put("name", tower.name());
         towerMap.put("ranks", tower.ranks().stream().map(rank -> {
             Map<String, Object> rankMap = new HashMap<>();
             rankMap.put("rank", rank.rank());
             rankMap.put("upgradeCost", rank.upgradeCost());
             rankMap.put("sellValue", rank.sellValue());
             rankMap.put("range", rank.range());
             rankMap.put("damage", rank.damage());
             rankMap.put("reloadSeconds", rank.reloadSeconds());
             rankMap.put("buildTimeTicks", rank.buildTimeTicks());
             return rankMap;
         }).toList());
         return towerMap;
     }).toList());
     return config;
 }
 
 private Map<String, Object> buildEnemiesConfig(List<EnemyTypeData> enemies) {
     Map<String, Object> config = new HashMap<>();
     config.put("enemies", enemies.stream().map(enemy -> {
         Map<String, Object> enemyMap = new HashMap<>();
         enemyMap.put("id", enemy.id());
         enemyMap.put("hp", enemy.hp());
         enemyMap.put("speed", enemy.speed());
         enemyMap.put("bounty", enemy.bounty());
         return enemyMap;
     }).toList());
     return config;
 }
}