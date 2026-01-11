package com.towerdefense.orchestrator.systems.lifecycle;

import org.springframework.stereotype.Component;

import com.towerdefense.domain.GameState;
import com.towerdefense.domain.StateEnum;
import com.towerdefense.orchestrator.systems.core.GameSystem;
import com.towerdefense.orchestrator.systems.core.SystemPriority;

/**
 * Gère la détection et la déclaration de fin de partie.
 * 
 * Responsabilités :
 * - Vérifie les conditions de victoire
 * - Vérifie les conditions de défaite
 * - Bascule le GameState en TERMINATED
 */
@Component
public class GameOverSystem implements GameSystem {
    
    private final LevelProgressionSystem levelProgressionSystem;
    
    public GameOverSystem(LevelProgressionSystem levelProgressionSystem) {
        this.levelProgressionSystem = levelProgressionSystem;
    }
    
    @Override
    public void process(GameState state, int tick) {
        // Vérifie la défaite (plus de vies)
        if (state.player().lives() <= 0) {
            state.setState(StateEnum.TERMINATED);
            return;
        }
        
        // Vérifie la victoire (toutes les vagues terminées + aucun ennemi)
        if (levelProgressionSystem.isLevelFinished() && state.enemies().isEmpty()) {
            state.setState(StateEnum.TERMINATED);
        }
    }
    
    @Override
    public SystemPriority priority() {
        return SystemPriority.LOW;
    }
    
    @Override
    public boolean shouldProcess(GameState state, int tick) {
        // Ne s'exécute que si le jeu n'est pas déjà terminé
        return state.getState() != StateEnum.TERMINATED;
    }
}