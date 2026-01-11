package com.towerdefense.orchestrator.systems.lifecycle;

import org.springframework.stereotype.Component;

import com.towerdefense.domain.GameState;
import com.towerdefense.orchestrator.systems.core.GameSystem;
import com.towerdefense.orchestrator.systems.core.SystemPriority;

/**
 * Gère l'avancement du temps pour toutes les entités du jeu.
 * 
 * Responsabilités :
 * - Décrémente les cooldowns des tours
 * - Gère les timers de construction
 * - Gère les timers d'upgrade
 */
@Component
public class TimeManagementSystem implements GameSystem {
    
    @Override
    public void process(GameState state, int tick) {
        // Tick toutes les tours (cooldowns, construction, upgrade)
        state.towers().forEach(tower -> tower.tick());
    }
    
    @Override
    public SystemPriority priority() {
        return SystemPriority.HIGHEST;
    }
}