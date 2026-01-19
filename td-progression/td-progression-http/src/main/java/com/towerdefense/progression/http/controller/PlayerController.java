package com.towerdefense.progression.http.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.towerdefense.progression.http.api.PlayerApi;
import com.towerdefense.progression.http.model.AvailableLevel;
import com.towerdefense.progression.http.model.GameConfig;
import com.towerdefense.progression.http.model.LevelCompletionRequest;
import com.towerdefense.progression.http.model.LevelCompletionResponse;
import com.towerdefense.progression.http.model.PlayerProgressResponse;
import com.towerdefense.progression.service.MetaGameService;

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
        List<AvailableLevel> dtos = levels.stream()
            .map(l -> {
            	AvailableLevel level = new AvailableLevel();
            	level.setId(l.id());
            	level.setName(l.name());
            	level.setUnlocked(true);
            	level.setCompleted(l.completed());
            	level.setStars(l.stars());
            	return level;
            })
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @Override
    public ResponseEntity<GameConfig> prepareLevel(@PathVariable("levelId") String levelId) {
        try {
        	Map<String, Object> level = metaGameService.prepareLevel(levelId);
        	GameConfig gameConfig = new GameConfig();
        	gameConfig.levelConfig(level.get("levelConfig"));
        	gameConfig.towersConfig(level.get("towersConfig"));
        	gameConfig.enemiesConfig(level.get("enemiesConfig"));
            return ResponseEntity.ok(gameConfig);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @Override
    public ResponseEntity<LevelCompletionResponse> completeLevel(
            @PathVariable("levelId") String levelId,
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
