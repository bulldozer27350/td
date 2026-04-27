package com.towerdefense.orchestrator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.towerdefense.domain.GameState;

import com.towerdefense.domain.StateEnum;
import com.towerdefense.engine.api.GameStateObserver;
import com.towerdefense.orchestrator.runtime.LevelScenario;
import com.towerdefense.orchestrator.systems.cleanup.DeadEntityCleanupSystem;
import com.towerdefense.orchestrator.systems.cleanup.OutOfBoundsCleanupSystem;
import com.towerdefense.orchestrator.systems.combat.CollisionSystem;
import com.towerdefense.orchestrator.systems.combat.ProjectileSystem;
import com.towerdefense.orchestrator.systems.combat.ShootingSystem;
import com.towerdefense.orchestrator.systems.core.GameSystem;
import com.towerdefense.orchestrator.systems.economy.LifeManagementSystem;
import com.towerdefense.orchestrator.systems.economy.RewardSystem;
import com.towerdefense.orchestrator.systems.lifecycle.EntityMovementSystem;
import com.towerdefense.orchestrator.systems.lifecycle.GameOverSystem;
import com.towerdefense.orchestrator.systems.lifecycle.LevelProgressionSystem;
import com.towerdefense.orchestrator.systems.lifecycle.TimeManagementSystem;
import com.towerdefense.orchestrator.systems.notification.ObserverNotificationSystem;

/**
 * Orchestrateur principal du moteur de jeu.
 * 
 * Responsabilité unique : Exécuter les systems dans le bon ordre.
 * 
 * Architecture : - Chaque system gère un aspect spécifique du jeu - Les systems
 * s'exécutent selon leur priorité - Le Sequencer reste simple et maintenable
 * 
 * Ordre d'exécution : 1. Time Management (HIGHEST) 2. Level Progression
 * (HIGHEST) 3. Entity Movement (HIGH) 4. Targeting (HIGH) 5. Shooting (NORMAL)
 * 6. Projectile Movement (NORMAL) 7. Collision Detection (LOW) 8. Reward
 * Distribution (LOW) 9. Life Management (LOW) 10. Game Over Check (LOW) 11.
 * Dead Entity Cleanup (LOWEST) 12. Out of Bounds Cleanup (LOWEST) 13. Observer
 * Notifications (LOWEST)
 */
@Component
@Scope("prototype")
public class Sequencer {


	// ========================
	// SYSTEMS (injectés par Spring)
	// ========================

	private final List<GameSystem> systems;

	// Systems avec état ou nécessitant un accès direct
	private final LevelProgressionSystem levelProgressionSystem;
	private final ObserverNotificationSystem observerNotificationSystem;

	/**
	 * Construit le Sequencer avec tous les systems nécessaires.
	 * 
	 * Spring injecte automatiquement tous les beans GameSystem.
	 */
	public Sequencer(TimeManagementSystem timeManagementSystem, LevelProgressionSystem levelProgressionSystem,
			EntityMovementSystem entityMovementSystem, ShootingSystem shootingSystem,
			ProjectileSystem projectileSystem, CollisionSystem collisionSystem, RewardSystem rewardSystem,
			LifeManagementSystem lifeManagementSystem, GameOverSystem gameOverSystem,
			DeadEntityCleanupSystem deadEntityCleanupSystem, OutOfBoundsCleanupSystem outOfBoundsCleanupSystem,
			ObserverNotificationSystem observerNotificationSystem) {
		this.levelProgressionSystem = levelProgressionSystem;
		this.observerNotificationSystem = observerNotificationSystem;
		// Initialise la liste des systems dans l'ordre de priorité
		this.systems = new ArrayList<>(List.of(timeManagementSystem, levelProgressionSystem, entityMovementSystem,
				shootingSystem, projectileSystem, collisionSystem, rewardSystem, lifeManagementSystem,
				gameOverSystem, deadEntityCleanupSystem, outOfBoundsCleanupSystem, observerNotificationSystem));

		// Trie les systems par priorité
		this.systems.sort(Comparator.comparing(system -> system.priority().getValue()));
	}

	// ========================
	// MÉTHODE PRINCIPALE
	// ========================

	/**
	 * Exécute un tick du moteur de jeu.
	 * 
	 * Cette méthode ne contient plus AUCUNE logique métier. Elle se contente
	 * d'orchestrer l'exécution des systems.
	 * 
	 * @param state l'état actuel du jeu (sera modifié)
	 * @param tick  le numéro du tick courant
	 * @param observers la liste des observateurs à notifier 
	 */
	public void tick(GameState state, int tick, List<GameStateObserver> observers) {
		// Vérifie si le jeu est en cours
		if (state.getState() != StateEnum.IN_PROGRESS) {
			return;
		}

		// Exécute chaque system dans l'ordre de priorité
		for (GameSystem system : systems) {
			if (system.shouldProcess(state, tick)) {
				system.process(state, tick, observers);
			}
		}

	}
	
	// ========================
	// MÉTHODES DE CONFIGURATION
	// ========================

	/**
	 * Définit le niveau à gérer.
	 * 
	 * @param level le scénario du niveau
	 */
	public void setLevel(LevelScenario level) {
		levelProgressionSystem.setLevel(level);
	}

	/**
	 * Ajoute un observateur des événements du jeu.
	 * 
	 * @param observer l'observateur à ajouter
	 */
	public void addObserver(GameStateObserver observer) {
		observerNotificationSystem.addObserver(observer);
	}

	/**
	 * Retire un observateur des événements du jeu.
	 * 
	 * @param observer l'observateur à retirer
	 */
	public void removeObserver(GameStateObserver observer) {
		observerNotificationSystem.removeObserver(observer);
	}

	// ========================
	// MÉTHODES UTILITAIRES
	// ========================

	/**
	 * Retourne la liste des systems actifs (pour debugging).
	 * 
	 * @return liste des systems dans l'ordre d'exécution
	 */
	public List<String> getSystemNames() {
		return systems.stream().map(GameSystem::name).toList();
	}

	/**
	 * Vérifie si le niveau est terminé.
	 * 
	 * @return true si toutes les vagues sont finies
	 */
	public boolean isLevelFinished() {
		return levelProgressionSystem.isLevelFinished();
	}

}
