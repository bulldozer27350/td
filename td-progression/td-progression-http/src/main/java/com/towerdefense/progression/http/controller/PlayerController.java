package com.towerdefense.progression.http.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.towerdefense.progression.domain.game.GameConfig;
import com.towerdefense.progression.http.api.PlayerApi;
import com.towerdefense.progression.http.mapper.PlayerHttpMapper;
import com.towerdefense.progression.http.model.AvailableLevel;
import com.towerdefense.progression.http.model.GameConfiguration;
import com.towerdefense.progression.http.model.LevelCompletionRequest;
import com.towerdefense.progression.http.model.LevelCompletionResponse;
import com.towerdefense.progression.http.model.PlayerProgressResponse;
import com.towerdefense.progression.service.MetaGameService;

@RestController
public class PlayerController implements PlayerApi {

    private final MetaGameService metaGameService;
    private final PlayerHttpMapper mapper;

    public PlayerController(MetaGameService metaGameService, PlayerHttpMapper mapper) {
        this.metaGameService = metaGameService;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<PlayerProgressResponse> getPlayerProgress() {
        var progress = this.metaGameService.getPlayerProgress();
        PlayerProgressResponse response = new PlayerProgressResponse();

        response.setCompletedLevels(progress.getCompletedLevelIds().size());
        response.setUnlockedUpgrades(new ArrayList<>(progress.getUnlockedUpgrades()));
        response.setUpgradePoints(progress.getUpgradePoints());

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<AvailableLevel>> getAvailableLevels() {
        List<com.towerdefense.progression.service.AvailableLevel> levels = this.metaGameService.getAvailableLevels();
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
        GameConfig config = this.metaGameService.prepareLevel(levelId);
        GameConfiguration gameConfig = new GameConfiguration();
        gameConfig.setLevelConfig(mapper.toHttp(config.level()));
        gameConfig.setTowersConfig(mapper.toTowersHttp(config.towers()));
        gameConfig.setEnemiesConfig(mapper.toEnemiesHttp(config.enemies()));
        return ResponseEntity.ok(gameConfig);
    }

    @Override
    public ResponseEntity<LevelCompletionResponse> completeLevel(@PathVariable("levelId") String levelId,
            @RequestBody LevelCompletionRequest request) {

        int previousPoints = this.metaGameService.getPlayerProgress().getUpgradePoints();
        this.metaGameService.onLevelComplete(levelId, request.getStars());
        int newPoints = this.metaGameService.getPlayerProgress().getUpgradePoints();
        int earned = newPoints - previousPoints;

        LevelCompletionResponse response = new LevelCompletionResponse();
        response.setUpgradePointsEarned(earned);
        response.setNewLevelsUnlocked(List.of()); // TODO : calculer les nouveaux niveaux débloqués
        response.setTotalUpgradePoints(newPoints);

        return ResponseEntity.ok(response);
    }
}
