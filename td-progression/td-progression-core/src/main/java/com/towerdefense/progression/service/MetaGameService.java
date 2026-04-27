package com.towerdefense.progression.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
import com.towerdefense.progression.model.TowerCapacity;
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
    private final List<String> sortedLevelIds;
    
    private final Map<String, PlayerSession> sessions = new ConcurrentHashMap<>();
    
    public MetaGameService(Path dataDirectory, UpgradeRegistry upgradeRegistry) throws IOException {
        this.dataLoader = new DataLoader(dataDirectory);
        this.upgradeRegistry = upgradeRegistry;
        this.towerModifier = new TowerDataModifier(upgradeRegistry);
        this.rewardCalculator = new RewardCalculator();
        this.configBuilder = new GameConfigBuilder();
        
        // Charger toutes les données statiques
        List<LevelData> levelList = dataLoader.loadLevels();
        List<TowerTypeData> towerList = dataLoader.loadTowers();
        List<EnemyTypeData> enemyList = dataLoader.loadEnemies();
        
        this.levels = levelList.stream()
            .collect(Collectors.toMap(l -> l.id(), l -> l));
        this.towers = towerList.stream()
            .collect(Collectors.toMap(TowerTypeData::id, t -> t));
        this.enemies = enemyList.stream()
            .collect(Collectors.toMap(EnemyTypeData::id, e -> e));
        
        // Liste ordonnée des niveaux pour réinitialiser la progression linéairement
        this.sortedLevelIds = levels.keySet().stream().sorted().toList();
        
        System.out.println("[META] Service initialized with " + levels.size() + " levels");
    }
    
    private PlayerSession getSession(String clientId) {
        return sessions.computeIfAbsent(clientId, id -> {
            try {
                PlayerProgress progress = dataLoader.loadPlayerProgress(id);
                LinearProgression progression = new LinearProgression(sortedLevelIds);
                // Restaurer les déblocages basés sur le profil chargé
                progression.updateUnlocks(progress.getCompletedLevelIds());
                return new PlayerSession(progress, progression);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load profile for " + id, e);
            }
        });
    }

    private void saveSession(String clientId, PlayerSession session) {
        try {
            dataLoader.savePlayerProgress(clientId, session.progress);
        } catch (IOException e) {
            System.err.println("[META] Failed to save profile for " + clientId + ": " + e.getMessage());
        }
    }
    
    // Récupère les niveaux disponibles pour le joueur
    public List<AvailableLevel> getAvailableLevels(String clientId) {
        PlayerSession session = getSession(clientId);
        return session.progression.getUnlockedLevelIds().stream()
            .map(levelId -> {
                LevelData level = levels.get(levelId);
                int stars = session.progress.getLevelStars(levelId);
                boolean completed = session.progress.hasCompleted(levelId);
                return new AvailableLevel(levelId, level.id(), completed, stars);
            })
            .toList();
    }
    
    // Prépare un niveau pour être joué (avec upgrades appliqués)
    public GameConfig prepareLevel(String clientId, String levelId) {
        PlayerSession session = getSession(clientId);
        LevelData level = levels.get(levelId);
        if (level == null) {
            throw new IllegalArgumentException("Level not found: " + levelId);
        }
        
        if (!session.progression.isUnlocked(levelId)) {
            throw new IllegalStateException("Level not unlocked for user " + clientId + ": " + levelId);
        }
        
        // Appliquer les upgrades sur les tours
        List<TowerTypeData> modifiedTowers = towers.values().stream()
            .map(tower -> towerModifier.applyUpgrades(tower, session.progress.getUnlockedUpgrades()))
            .filter(t->level.towerCapacities().stream().map(TowerCapacity::towerTypeId).toList().contains(t.id()))
            .toList();
        
        List<EnemyTypeData> enemyList = new ArrayList<>(enemies.values());
        
        System.out.println("[META] Preparing level: " + levelId + " for player " + clientId);
        
        return configBuilder.buildGameConfig(level, modifiedTowers, enemyList);
    }
    
    // Traite le résultat d'une partie terminée
    public void onLevelComplete(String clientId, String levelId, int stars) {
        PlayerSession session = getSession(clientId);
        System.out.println("[META] Level completed by " + clientId + ": " + levelId + " with " + stars + " stars");
        
        int previousStars = session.progress.getLevelStars(levelId);
        session.progress.completeLevel(levelId, stars);
        
        // Récompenses uniquement si amélioration du score
        if (stars > previousStars) {
            int newStars = stars - previousStars;
            var reward = rewardCalculator.calculateRewards(newStars);
            session.progress.addUpgradePoints(reward.upgradePoints());
        }
        
        // Mettre à jour les niveaux débloqués
        session.progression.updateUnlocks(session.progress.getCompletedLevelIds());
        
        saveSession(clientId, session);
    }
    
    // Acheter un upgrade
    public boolean purchaseUpgrade(String clientId, String upgradeId) {
        PlayerSession session = getSession(clientId);
        var upgrade = upgradeRegistry.getUpgrade(upgradeId);
        if (upgrade == null) return false;
        
        boolean success = session.progress.unlockUpgrade(upgradeId, upgrade.getCost());
        if (success) {
            saveSession(clientId, session);
        }
        return success;
    }
    
    public PlayerProgress getPlayerProgress(String clientId) {
        return getSession(clientId).progress;
    }
    
    public List<TowerUpgrade> getAllUpgrades() {
        return upgradeRegistry.getAllUpgrades();
    }
    
    public List<TowerUpgrade> getUpgradesForTower(String towerType) {
        return upgradeRegistry.getUpgradesForTower(towerType);
    }

    private static class PlayerSession {
        final PlayerProgress progress;
        final LinearProgression progression;

        PlayerSession(PlayerProgress progress, LinearProgression progression) {
            this.progress = progress;
            this.progression = progression;
        }
    }
}
