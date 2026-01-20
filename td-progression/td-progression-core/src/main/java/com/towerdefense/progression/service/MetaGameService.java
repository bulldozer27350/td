package com.towerdefense.progression.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.towerdefense.progression.domain.game.GameConfig;
import com.towerdefense.progression.domain.player.PlayerProgress;
import com.towerdefense.progression.domain.progression.LinearProgression;
import com.towerdefense.progression.domain.reward.RewardCalculator;
import com.towerdefense.progression.domain.upgrade.TowerUpgrade;
import com.towerdefense.progression.domain.upgrade.UpgradeRegistry;
import com.towerdefense.progression.integration.GameConfigBuilder;
import com.towerdefense.progression.loader.DataLoader;
import com.towerdefense.progression.model.EnemyTypeData;
import com.towerdefense.progression.model.LevelData;
import com.towerdefense.progression.model.TowerTypeData;
import com.towerdefense.progression.modifier.TowerDataModifier;

public class MetaGameService {
    private final DataLoader dataLoader;
    private final UpgradeRegistry upgradeRegistry;
    private final TowerDataModifier towerModifier;
    private final RewardCalculator rewardCalculator;
    private final GameConfigBuilder configBuilder;
    
    private final Map<String, LevelData> levels;
    private final Map<String, TowerTypeData> towers;
    private final Map<String, EnemyTypeData> enemies;
    
    private final LinearProgression progression;
    private final PlayerProgress playerProgress;
    
    public MetaGameService(Path dataDirectory, UpgradeRegistry upgradeRegistry) throws IOException {
        this.dataLoader = new DataLoader(dataDirectory);
        this.upgradeRegistry = upgradeRegistry;
        this.towerModifier = new TowerDataModifier(upgradeRegistry);
        this.rewardCalculator = new RewardCalculator();
        this.configBuilder = new GameConfigBuilder();
        
        // Charger toutes les données
        List<LevelData> levelList = dataLoader.loadLevels();
        List<TowerTypeData> towerList = dataLoader.loadTowers();
        List<EnemyTypeData> enemyList = dataLoader.loadEnemies();
        
        this.levels = levelList.stream()
            .collect(Collectors.toMap(l -> l.id(), l -> l));
        this.towers = towerList.stream()
            .collect(Collectors.toMap(TowerTypeData::id, t -> t));
        this.enemies = enemyList.stream()
            .collect(Collectors.toMap(EnemyTypeData::id, e -> e));
        
        // Initialiser la progression (tri alphabétique des niveaux)
        List<String> sortedLevelIds = levels.keySet().stream().sorted().toList();
        this.progression = new LinearProgression(sortedLevelIds);
        
        this.playerProgress = new PlayerProgress();
        
        logStatus();
    }
    
    // Récupère les niveaux disponibles pour le joueur
    public List<AvailableLevel> getAvailableLevels() {
        return progression.getUnlockedLevelIds().stream()
            .map(levelId -> {
                LevelData level = levels.get(levelId);
                int stars = playerProgress.getLevelStars(levelId);
                boolean completed = playerProgress.hasCompleted(levelId);
                return new AvailableLevel(levelId, level.id(), completed, stars);
            })
            .toList();
    }
    
    // Prépare un niveau pour être joué (avec upgrades appliqués)
    public GameConfig prepareLevel(String levelId) {
        LevelData level = levels.get(levelId);
        if (level == null) {
            throw new IllegalArgumentException("Level not found: " + levelId);
        }
        
        if (!progression.isUnlocked(levelId)) {
            throw new IllegalStateException("Level not unlocked: " + levelId);
        }
        
        // Appliquer les upgrades sur les tours
        List<TowerTypeData> modifiedTowers = towers.values().stream()
            .map(tower -> towerModifier.applyUpgrades(tower, playerProgress.getUnlockedUpgrades()))
            .toList();
        
        List<EnemyTypeData> enemyList = new ArrayList<>(enemies.values());
        
        System.out.println("[META] Preparing level: " + levelId);
        logTowerUpgrades(modifiedTowers);
        
        return configBuilder.buildGameConfig(level, modifiedTowers, enemyList);
    }
    
    // Traite le résultat d'une partie terminée
    public void onLevelComplete(String levelId, int stars) {
        System.out.println("[META] Level completed: " + levelId + " with " + stars + " stars");
        
        int previousStars = playerProgress.getLevelStars(levelId);
        playerProgress.completeLevel(levelId, stars);
        
        // Récompenses uniquement si amélioration du score
        if (stars > previousStars) {
            int newStars = stars - previousStars;
            var reward = rewardCalculator.calculateRewards(newStars);
            playerProgress.addUpgradePoints(reward.upgradePoints());
            System.out.println("[META] Earned " + reward.upgradePoints() + " upgrade points");
        }
        
        // Mettre à jour les niveaux débloqués
        progression.updateUnlocks(playerProgress.getCompletedLevelIds());
        
        logStatus();
    }
    
    // Acheter un upgrade
    public boolean purchaseUpgrade(String upgradeId) {
        var upgrade = upgradeRegistry.getUpgrade(upgradeId);
        if (upgrade == null) {
            System.err.println("[META] Unknown upgrade: " + upgradeId);
            return false;
        }
        
        boolean success = playerProgress.unlockUpgrade(upgradeId, upgrade.getCost());
        if (success) {
            System.out.println("[META] Purchased: " + upgrade.getName() + 
                             " (cost: " + upgrade.getCost() + " points)");
            logStatus();
        } else {
            System.out.println("[META] Cannot purchase: insufficient points");
        }
        return success;
    }
    
    // Afficher le statut
    public void logStatus() {
        System.out.println("\n=== META GAME STATUS ===");
        System.out.println("Upgrade Points: " + playerProgress.getUpgradePoints());
        
        var unlocked = progression.getUnlockedLevelIds();
        long completed = playerProgress.getCompletedLevelIds().size();
        System.out.println("Levels: " + completed + " completed, " + unlocked.size() + " unlocked");
        
        String suggested = progression.getNextSuggestedLevel(playerProgress.getCompletedLevelIds());
        if (suggested != null) {
            System.out.println("Next suggested: " + suggested);
        }
        System.out.println("========================\n");
    }
    
    private void logTowerUpgrades(List<TowerTypeData> towers) {
        System.out.println("[META] Towers with upgrades:");
        for (TowerTypeData tower : towers) {
            System.out.println("  " + tower.name() + " (" + tower.id() + "):");
            for (var level : tower.levels()) {
                System.out.printf("    Level %d: damage=%d, range=%.2f, reload=%.2fs%n",
                    level.level(), level.damage(), level.range(), level.reloadSeconds());
            }
        }
    }

    public PlayerProgress getPlayerProgress() {
        return playerProgress;
    }
    
    public List<TowerUpgrade> getAllUpgrades() {
        return upgradeRegistry.getAllUpgrades();
    }
    
    public List<TowerUpgrade> getUpgradesForTower(String towerType) {
        return upgradeRegistry.getUpgradesForTower(towerType);
    }
    
}
