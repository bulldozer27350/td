package com.towerdefense.domain.map;

import java.util.ArrayList;
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

	/**
	 * Crée un chemin avec une liste de points de passage
	 * 
	 * @param waypoints la liste des points de passage du chemin
	 */
	public EnemyPath(String identifier, List<Position> waypoints) {
		this.identifier = identifier;
		if (waypoints == null || waypoints.isEmpty()) {
			throw new IllegalArgumentException("Path must contain at least one waypoint");
		}
		this.waypoints = new ArrayList<Position>(waypoints);
	}

	/**
	 * Récupère l'identifiant du chemin
	 * 
	 * @return l'identifiant du chemin
	 */
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
	
	public List<Position> getWay() {
		return waypoints;
	}
	
	public void setWaypoints(List<Position> newWaypoints) {
        this.waypoints.clear();
        this.waypoints.addAll(newWaypoints);
        this.cachedTotalLength = null; // Invalidate cached length
    }
	
	/**
	 * Calcule la longueur totale du chemin.
	 * La longueur est mise en cache lors du premier appel.
	 * 
	 * @return longueur totale en unités
	 */
	private Double cachedTotalLength = null;
	
	public double totalLength() {
	    if (cachedTotalLength == null) {
	        double total = 0.0;
	        for (int i = 0; i < waypoints.size() - 1; i++) {
	            Position p1 = waypoints.get(i);
	            Position p2 = waypoints.get(i + 1);
	            double dx = p2.x() - p1.x();
	            double dy = p2.y() - p1.y();
	            total += Math.sqrt(dx * dx + dy * dy);
	        }
	        cachedTotalLength = total;
	    }
	    return cachedTotalLength;
	}
}
