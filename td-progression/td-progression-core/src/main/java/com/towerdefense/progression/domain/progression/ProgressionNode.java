package com.towerdefense.progression.domain.progression;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProgressionNode {
    private final String levelId;
    private final List<String> requirements; // IDs des niveaux prérequis
    private boolean unlocked;
    
    public ProgressionNode(String levelId, List<String> requirements) {
        this.levelId = levelId;
        this.requirements = new ArrayList<>(requirements);
        this.unlocked = false;
    }
    
    public boolean canUnlock(Set<String> completedLevels) {
        return requirements.stream().allMatch(completedLevels::contains);
    }
    
    // Getters/Setters
    public String getLevelId() { return levelId; }
    public boolean isUnlocked() { return unlocked; }
    public void setUnlocked(boolean unlocked) { this.unlocked = unlocked; }
}
