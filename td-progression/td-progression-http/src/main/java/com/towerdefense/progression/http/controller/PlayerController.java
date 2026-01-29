package com.towerdefense.progression.http.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.towerdefense.progression.domain.game.GameConfig;
import com.towerdefense.progression.http.api.PlayerApi;
import com.towerdefense.progression.http.model.AttackConfiguration;
import com.towerdefense.progression.http.model.AvailableLevel;
import com.towerdefense.progression.http.model.EnemiesConfiguration;
import com.towerdefense.progression.http.model.EnemyTypeConfiguration;
import com.towerdefense.progression.http.model.GameConfiguration;
import com.towerdefense.progression.http.model.LevelCompletionRequest;
import com.towerdefense.progression.http.model.LevelCompletionResponse;
import com.towerdefense.progression.http.model.LevelConfiguration;
import com.towerdefense.progression.http.model.MapDimensionsConfiguration;
import com.towerdefense.progression.http.model.PathConfiguration;
import com.towerdefense.progression.http.model.PlayerProgressResponse;
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
import com.towerdefense.progression.service.MetaGameService;

import jakarta.validation.Valid;

@RestController
public class PlayerController implements PlayerApi {

    private final MetaGameService metaGameService;

    public PlayerController(MetaGameService metaGameService) {
        this.metaGameService = metaGameService;
    }

    @Override
    public ResponseEntity<PlayerProgressResponse> getPlayerProgress() {
        var progress = metaGameService.getPlayerProgress();
        PlayerProgressResponse response = new PlayerProgressResponse();

        response.setCompletedLevels(progress.getCompletedLevelIds().size());
        response.setUnlockedUpgrades(new ArrayList<>(progress.getUnlockedUpgrades()));
        response.setUpgradePoints(progress.getUpgradePoints());

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<AvailableLevel>> getAvailableLevels() {
        List<com.towerdefense.progression.service.AvailableLevel> levels = metaGameService.getAvailableLevels();
        List<AvailableLevel> dtos = levels.stream().map(l -> {
            AvailableLevel level = new AvailableLevel();
            level.setId(l.id());
            level.setName(l.name());
            level.setUnlocked(true);
            level.setCompleted(l.completed());
            level.setStars(l.stars());
            return level;
        }).toList();
        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<GameConfiguration> prepareLevel(@PathVariable("levelId") String levelId) {
        try {
            GameConfig config = metaGameService.prepareLevel(levelId);
            GameConfiguration gameConfig = new GameConfiguration();
            gameConfig.setLevelConfig(toHttp(config.level()));
            gameConfig.setTowersConfig(toTowersHttp(config.towers()));
            gameConfig.setEnemiesConfig(toEnemiesHttp(config.enemies()));
            return ResponseEntity.ok(gameConfig);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private EnemiesConfiguration toEnemiesHttp(List<EnemyTypeData> enemies) {
        return new EnemiesConfiguration(enemies.stream().map(e->toHttp(e)).toList());
    }

    private EnemyTypeConfiguration toHttp(EnemyTypeData e) {
        return new EnemyTypeConfiguration(e.id(), e.hp(), e.speed(), e.bounty());
    }

    private TowersConfiguration toTowersHttp(List<TowerTypeData> towers) {
        return new TowersConfiguration(towers.stream().map(t->toHttp(t)).toList());
    }
    
    private TowerTypeConfiguration toHttp(TowerTypeData t) {
        return new TowerTypeConfiguration(t.id(), t.name(), t.ranks().stream().map(tl->toHttp(tl)).toList());
    }

    private TowerRankConfiguration toHttp(TowerRank tl) {
        return new TowerRankConfiguration(tl.rank(), tl.upgradeCost(), tl.sellValue(), tl.range(), tl.damage(), tl.reloadSeconds(), tl.buildTimeTicks());
    }

    private LevelConfiguration toHttp(LevelData level) {
        return new LevelConfiguration(level.id(),
                new MapDimensionsConfiguration(level.map().width(), level.map().height()), level.startingLives(),
                level.startingMoney(), level.attacks().stream().map(a -> toHttp(a)).toList(),
                level.towerCapacities().stream().map(t -> toHttp(t)).toList(),
                level.paths().stream().map(p -> toHttp(p)).toList());
    }

    private TowerCapacityConfiguration toHttp(TowerCapacity t) {
        return new TowerCapacityConfiguration(t.towerTypeId(), t.maxRank());
    }

    private @Valid AttackConfiguration toHttp(Attack attack) {
        return new AttackConfiguration(attack.id(), attack.waves().stream().map(w -> toHttp(w)).toList());
    }

    private @Valid WaveConfiguration toHttp(Wave wave) {
        return new WaveConfiguration(wave.id(), wave.startTick(), wave.spawnInterval(), wave.count(), wave.enemyType(),
                wave.pathId());
    }

    private @Valid PathConfiguration toHttp(LevelPath path) {
        return new PathConfiguration(path.id(), path.points().stream().map(p -> toHttp(p)).toList());
    }

    private @Valid PointConfiguration toHttp(Point point) {
        return new PointConfiguration(point.x(), point.y());
    }

    @Override
    public ResponseEntity<LevelCompletionResponse> completeLevel(@PathVariable("levelId") String levelId,
            @RequestBody LevelCompletionRequest request) {

        int previousPoints = metaGameService.getPlayerProgress().getUpgradePoints();
        metaGameService.onLevelComplete(levelId, request.getStars());
        int newPoints = metaGameService.getPlayerProgress().getUpgradePoints();
        int earned = newPoints - previousPoints;

        LevelCompletionResponse response = new LevelCompletionResponse();
        response.setUpgradePointsEarned(earned);
        response.setNewLevelsUnlocked(List.of()); // TODO : calculer les nouveaux niveaux débloqués
        response.setTotalUpgradePoints(newPoints);

        return ResponseEntity.ok(response);
    }

}
