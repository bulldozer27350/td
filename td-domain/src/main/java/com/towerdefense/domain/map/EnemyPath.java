package com.towerdefense.domain.map;

import java.util.List;

import com.towerdefense.domain.Position;

/**
 * Gère un chemin sur une carte. Le chemin a un point de départ et un point
 * d'arrivée, mais a aussi des points de passage pour ne pas gérer qu'un seul
 * déplacement en ligne droite.
 */
public class EnemyPath {

	private final List<Position> waypoints;
	private String identifier;

	public EnemyPath(String identifier, List<Position> waypoints) {
		this.identifier = identifier;
		if (waypoints == null || waypoints.isEmpty()) {
			throw new IllegalArgumentException("Path must contain at least one waypoint");
		}
		this.waypoints = List.copyOf(waypoints);
	}

	
	public String getIdentifier() {
		return identifier;
	}
	
	/**
	 * Récupère le point de départ du chemin (et pas forcément le point de départ de
	 * la direction en cours)
	 * 
	 * @return
	 */
	public Position startPosition() {
		return waypoints.get(0);
	}

	/**
	 * Donne le point de passage correspondant à l'index donné.
	 * 
	 * @param index l'index dans la liste des points de passage
	 * @return la position (x;y) du point de passage correspondant à l'index
	 */
	public Position waypoint(int index) {
		return waypoints.get(index);
	}

	/**
	 * Donne le nombre de changement de direction sur le chemin (puisque les
	 * positions sont des points de passage sur le chemin au moment où, en toute
	 * logique, le chemin changera de direction)
	 * 
	 * @return
	 */
	public int size() {
		return waypoints.size();
	}
}
