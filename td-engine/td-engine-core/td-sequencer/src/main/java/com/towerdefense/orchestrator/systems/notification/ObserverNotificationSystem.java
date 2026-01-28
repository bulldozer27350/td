package com.towerdefense.orchestrator.systems.notification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.towerdefense.domain.GameState;
import com.towerdefense.domain.StateEnum;
import com.towerdefense.engine.api.GameStateObserver;
import com.towerdefense.orchestrator.GameStateMapper;
import com.towerdefense.orchestrator.systems.core.GameSystem;
import com.towerdefense.orchestrator.systems.core.SystemPriority;

/**
 * Notifie les observateurs des changements d'état.
 * 
 * Responsabilités :
 * - Envoie les notifications de mise à jour du state
 * - Envoie les notifications de victoire/défaite
 * - Gère la liste des observateurs
 */
@Component
public class ObserverNotificationSystem implements GameSystem {
    
    private final List<GameStateObserver> observers = new ArrayList<>();
    
    @Override
    public void process(GameState state, int tick, List<GameStateObserver> observers) {
        // Notification de mise à jour du state
        notifyStateUpdated(state, tick);
        
        // Notification de fin de partie si applicable
        if (state.getState() == StateEnum.TERMINATED) {
            notifyGameOver(state);
        }
    }
    
    private void notifyStateUpdated(GameState state, int tick) {
        observers.forEach(observer -> 
            observer.onStateUpdated(GameStateMapper.toDTO(state), tick)
        );
    }
    
    private void notifyGameOver(GameState state) {
        boolean playerWon = state.player().isAlive() && state.enemies().isEmpty();
        
        if (playerWon) {
            observers.forEach(observer -> 
                observer.onGameWon(GameStateMapper.toDTO(state))
            );
        } else {
            observers.forEach(observer -> 
                observer.onGameLoose()
            );
        }
    }
    
    public void addObserver(GameStateObserver observer) {
        observers.add(observer);
    }
    
    public void removeObserver(GameStateObserver observer) {
        observers.remove(observer);
    }
    
    @Override
    public SystemPriority priority() {
        return SystemPriority.LOWEST;
    }
}