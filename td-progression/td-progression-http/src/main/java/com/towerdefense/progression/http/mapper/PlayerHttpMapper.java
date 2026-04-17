package com.towerdefense.progression.http.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.towerdefense.progression.http.model.AttackConfiguration;
import com.towerdefense.progression.http.model.EnemiesConfiguration;
import com.towerdefense.progression.http.model.EnemyTypeConfiguration;
import com.towerdefense.progression.http.model.LevelConfiguration;
import com.towerdefense.progression.http.model.MapDimensionsConfiguration;
import com.towerdefense.progression.http.model.PathConfiguration;
import com.towerdefense.progression.http.model.PointConfiguration;
import com.towerdefense.progression.http.model.TowerCapacityConfiguration;
import com.towerdefense.progression.http.model.TowerRankConfiguration;
import com.towerdefense.progression.http.model.TowerTypeConfiguration;
import com.towerdefense.progression.http.model.TowersConfiguration;
import com.towerdefense.progression.http.model.WaveConfiguration;
import com.towerdefense.progression.model.Attack;
import com.towerdefense.progression.model.EnemyTypeData;
import com.towerdefense.progression.model.LevelData;
import com.towerdefense.progression.model.LevelPath;
import com.towerdefense.progression.model.Point;
import com.towerdefense.progression.model.TowerCapacity;
import com.towerdefense.progression.model.TowerRank;
import com.towerdefense.progression.model.TowerTypeData;
import com.towerdefense.progression.model.Wave;

@Component
public class PlayerHttpMapper {

    public EnemiesConfiguration toEnemiesHttp(List<EnemyTypeData> enemies) {
        return new EnemiesConfiguration(enemies.stream().map(this::toHttp).toList());
    }

    public EnemyTypeConfiguration toHttp(EnemyTypeData e) {
        return new EnemyTypeConfiguration(e.id(), e.hp(), e.speed(), e.bounty());
    }

    public TowersConfiguration toTowersHttp(List<TowerTypeData> towers) {
        return new TowersConfiguration(towers.stream().map(this::toHttp).toList());
    }
    
    public TowerTypeConfiguration toHttp(TowerTypeData t) {
        return new TowerTypeConfiguration(t.id(), t.name(), t.ranks().stream().map(this::toHttp).toList());
    }

    public TowerRankConfiguration toHttp(TowerRank tl) {
        return new TowerRankConfiguration(tl.rank(), tl.upgradeCost(), tl.sellValue(), tl.range(), tl.damage(), tl.reloadSeconds(), tl.buildTimeTicks());
    }

    public LevelConfiguration toHttp(LevelData level) {
        return new LevelConfiguration(
                level.id(),
                new MapDimensionsConfiguration(level.map().width(), level.map().height()), 
                level.startingLives(),
                level.startingMoney(), 
                level.attacks().stream().map(this::toHttp).toList(),
                level.towerCapacities().stream().map(this::toHttp).toList(),
                level.paths().stream().map(this::toHttp).toList());
    }

    public TowerCapacityConfiguration toHttp(TowerCapacity t) {
        return new TowerCapacityConfiguration(t.towerTypeId(), t.maxRank());
    }

    public AttackConfiguration toHttp(Attack attack) {
        return new AttackConfiguration(attack.id(), attack.waves().stream().map(this::toHttp).toList());
    }

    public WaveConfiguration toHttp(Wave wave) {
        return new WaveConfiguration(wave.id(), wave.startTick(), wave.spawnInterval(), wave.count(), wave.enemyType(), wave.pathId());
    }

    public PathConfiguration toHttp(LevelPath path) {
        return new PathConfiguration(path.id(), path.points().stream().map(this::toHttp).toList());
    }

    public PointConfiguration toHttp(Point point) {
        return new PointConfiguration(point.x(), point.y());
    }
}
