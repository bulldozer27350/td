package com.towerdefense.progression.domain.player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PlayerProgress {
    private final Map<String, LevelCompletion> completedLevels = new HashMap<>();
    private final Set<String> unlockedUpgrades = new HashSet<>();
    private int upgradePoints = 0;
    
    // Compléter un niveau
    public void completeLevel(String levelId, int stars) {
        LevelCompletion existing = completedLevels.get(levelId);
        int previousStars = existing != null ? existing.stars() : 0;
        
        // Ne garder que le meilleur score
        if (stars > previousStars) {
            completedLevels.put(levelId, new LevelCompletion(levelId, stars, System.currentTimeMillis()));
        }
    }
    
    // Débloquer un upgrade
    public boolean unlockUpgrade(String upgradeId, int cost) {
        if (upgradePoints >= cost && !unlockedUpgrades.contains(upgradeId)) {
            upgradePoints -= cost;
            unlockedUpgrades.add(upgradeId);
            return true;
        }
        return false;
    }
    
    // Ajouter des points d'upgrade
    public void addUpgradePoints(int points) {
        this.upgradePoints += points;
    }
    
    // Getters
    public Set<String> getCompletedLevelIds() {
        return new HashSet<>(completedLevels.keySet());
    }
    
    public int getLevelStars(String levelId) {
        LevelCompletion completion = completedLevels.get(levelId);
        return completion != null ? completion.stars() : 0;
    }
    
    public Set<String> getUnlockedUpgrades() {
        return new HashSet<>(unlockedUpgrades);
    }
    
    public int getUpgradePoints() {
        return upgradePoints;
    }
    
    public boolean hasCompleted(String levelId) {
        return completedLevels.containsKey(levelId);
    }
    
}
