package com.towerdefense.domain.dynamik.enemy;

import com.towerdefense.domain.EntityId;
import com.towerdefense.domain.GameObject;
import com.towerdefense.domain.Health;
import com.towerdefense.domain.Position;
import com.towerdefense.domain.map.EnemyPath;

/**
 * Un ennemi est un élément du jeu. C'est une entité qui se déplace sur un
 * chemin donné, qui possède un nombre de points de vie, une vitesse de
 * déplacement et bien sûr une position actuelle sur la carte via une
 * Position(x;y)
 * 
 * Son déplacement est géré par un index sur son chemin pour qu'il sache quelle
 * est sa prochaine position à atteindre.
 * 
 * La gestion du temps est donné par la méthode tick qui permet de savoir s'il
 * peut atteindre sa prochaine position, avec quelle vitesse de déplacement etc
 * ...
 */
public class Enemy implements GameObject {
	private final EntityId id;
	private Position position;
	private final Health health;
	private final double speed; // cases per second
	private final EnemyPath path;
	private int waypointIndex;
	private int bounty;

	public Enemy(EntityId id, EnemyPath path, int hp, double speed, int bounty) {
		this.id = id;
		this.path = path;
		this.position = path.startPosition();
		this.health = new Health(hp);
		this.speed = speed;
		this.bounty = bounty;
	}

	public EntityId id() {
		return this.id;
	}

	public Position position() {
		return this.position;
	}

	public Health health() {
		return this.health;
	}

	public double speed() {
		return this.speed;
	}
	
	public int bounty() {
		return bounty;
	}

	/**
	 * Permet de savoir comment il peut atteindre sa prochaine destination (next est
	 * un point cible sur le chemin, pas nécessairement le prochain mouvement : next
	 * peut s'atteindre en plusieurs mouvements qui seront a priori tous dans la
	 * même direction)
	 * 
	 * Sa capacité à atteindre next est reliée à sa vitesse de déplacement, au fait
	 * qu'il soit encore en vie ou non, à la distance qui le sépare de next, etc ...
	 * 
	 * @param next
	 */
	private void moveTowards(Position next) {
		double dx = next.x() - this.position.x();
		double dy = next.y() - this.position.y();
		double dist = Math.sqrt(dx * dx + dy * dy);
		if (dist == 0)
			return;

		double move = Math.min(this.speed, dist);
		this.position = new Position(this.position.x() + dx / dist * move, this.position.y() + dy / dist * move);
	}

	/**
	 * Gère l'action suivante. Ici, il ne s'agit pour un ennemi que d'avancer vers
	 * la prochaine direction.
	 */
	public void tick() {
		if (!this.health.isDead() && !this.isAtEnd()) {
			Position target = this.path.waypoint(this.waypointIndex);
			this.moveTowards(target);

			if (this.position.equals(target)) {
				this.waypointIndex++;
			}
		}
	}

	public boolean isAtEnd() {
		return this.waypointIndex >= this.path.size();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getName());
		builder.append(": {id:");
		builder.append(id);
		builder.append(", position:");
		builder.append(position);
		builder.append(", health:");
		builder.append(health);
		builder.append(", speed:");
		builder.append(speed);
		builder.append("}");
		return builder.toString();
	}
}
