package com.towerdefense.services;

import com.towerdefense.domain.GameState;
import com.towerdefense.domain.intentions.BuildTowerIntention;
import com.towerdefense.domain.intentions.SellTowerIntention;
import com.towerdefense.domain.intentions.UpgradeTowerIntention;

/**
 * Offre des services de gestion de tours : achat d'une tour, évolution, vente,
 * ...
 */
public interface TowerServices {

	/**
	 * Vérifie et applique la construction d'une tour dans le jeu.
	 * 
	 * @param state  l'état du jeu à un instant donné.
	 * @param intent intention de construction, composée d'un identifiant de joueur,
	 *               d'un type de tour et d'une position.
	 * @return true si la construction a bien pu être réalisée (la tour sera
	 *         également ajoutée au GameState), false dans le cas contraire.
	 */
	boolean attemptBuildTower(GameState state, BuildTowerIntention intent);
	
	/**
	 * Vérifie et applique une vente d'une tour dans le jeu.
	 * 
	 * @param state  l'état du jeu à un instant donné.
	 * @param intent intention de vente, composée d'un identifiant de joueur et d'un
	 *               identifiant de tour
	 * @return true si la vente a bien pu être réalisée (la tour sera également
	 *         supprimée du GameState), false dans le cas contraire.
	 */
	boolean attemptSellTower(GameState state, SellTowerIntention intent);

	/**
	 * Vérifie et applique une montée de niveau d'une tour dans le jeu.
	 * 
	 * @param state  l'état du jeu à un instant donné.
	 * @param intent intention de faire évoluer une tour sur le niveau suivant,
	 *               composée d'un identifiant de joueur et d'un identifiant de
	 *               tour.
	 * @return true si la mise à niveau de la tour a été réalisée (le GameState aura
	 *         été mis à jour), false dans le cas contraire.
	 */
	boolean attemptUpgradeTower(GameState state, UpgradeTowerIntention intent);
}
