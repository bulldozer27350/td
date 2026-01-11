package com.towerdefense.orchestrator.systems.lifecycle;

import org.springframework.stereotype.Component;

import com.towerdefense.domain.GameState;
import com.towerdefense.orchestrator.runtime.LevelScenario;
import com.towerdefense.orchestrator.systems.core.GameSystem;
import com.towerdefense.orchestrator.systems.core.SystemPriority;

/**
 * Gère la progression du niveau et le spawn des ennemis.
 * 
 * Responsabilités :
 * - Fait avancer les vagues d'ennemis
 * - Spawn les ennemis selon le timing défini
 * - Met à jour le LevelProgress
 */
@Component
public class LevelProgressionSystem implements GameSystem {
    
    private LevelScenario level;
    
    @Override
    public void process(GameState state, int tick) {
        if (level == null) {
            return;
        }
        
        // Fait progresser le scénario du niveau
        level.tick(state, tick);
        
        // Met à jour le progress dans le state
        state.setLevelProgress(level.snapshot());
    }
    
    @Override
    public SystemPriority priority() {
        return SystemPriority.HIGHEST;
    }
    
    @Override
    public boolean shouldProcess(GameState state, int tick) {
        return level != null && !level.isFinished();
    }
    
    /**
     * Définit le niveau à gérer.
     */
    public void setLevel(LevelScenario level) {
        this.level = level;
    }
    
    /**
     * Vérifie si le niveau est terminé.
     */
    public boolean isLevelFinished() {
        return level != null && level.isFinished();
    }
}