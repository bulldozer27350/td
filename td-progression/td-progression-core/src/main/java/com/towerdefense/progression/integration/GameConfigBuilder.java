package com.towerdefense.progression.integration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.towerdefense.progression.model.Attack;
import com.towerdefense.progression.model.EnemyTypeData;
import com.towerdefense.progression.model.LevelData;
import com.towerdefense.progression.model.Path;
import com.towerdefense.progression.model.TowerCapacity;
import com.towerdefense.progression.model.TowerTypeData;
import com.towerdefense.progression.model.Wave;

//Construit le GameConfig au format attendu par td-engine
public class GameConfigBuilder {
 
 public Map<String, Object> buildGameConfig(
         LevelData level,
         List<TowerTypeData> towers,
         List<EnemyTypeData> enemies) {
     
     Map<String, Object> config = new HashMap<>();
     config.put("levelConfig", buildLevelConfig(level));
     config.put("towersConfig", buildTowersConfig(towers));
     config.put("enemiesConfig", buildEnemiesConfig(enemies));
     
     return config;
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
 
 private Map<String, Object> buildMap(com.towerdefense.progression.model.Map map) {
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
         capMap.put("maxLevel", cap.maxLevel());
         return capMap;
     }).toList();
 }
 
 private Map<String, Object> buildPathsConfig(List<Path> paths) {
     Map<String, Object> config = new HashMap<>();
     for (Path p : paths) {
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
         towerMap.put("levels", tower.levels().stream().map(level -> {
             Map<String, Object> levelMap = new HashMap<>();
             levelMap.put("level", level.level());
             levelMap.put("upgradeCost", level.upgradeCost());
             levelMap.put("sellValue", level.sellValue());
             levelMap.put("range", level.range());
             levelMap.put("damage", level.damage());
             levelMap.put("reloadSeconds", level.reloadSeconds());
             levelMap.put("buildTimeTicks", level.buildTimeTicks());
             return levelMap;
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