package com.towerdefense.domain.dynamik.enemy;

import com.towerdefense.domain.EntityId;
import com.towerdefense.domain.GameObject;
import com.towerdefense.domain.Health;
import com.towerdefense.domain.Position;
import com.towerdefense.domain.map.EnemyPath;

/**
 * An enemy is a game element. It is an entity that moves along a given path,
 * has a number of health points, a movement speed, and of course a current
 * position on the map via a Position(x;y).
 * 
 * Its movement is managed by an index on its path so that it knows what its
 * next position to reach is.
 * 
 * The time management is given by the tick method which allows to know if it
 * can reach its next position, with what movement speed, etc...
 */
public class Enemy implements GameObject {
	private final EntityId id;
	private Position position;
	private final Health health;
	private final double speed; // cases per second
	private final EnemyPath path;
	private int waypointIndex;
	private int bounty;

	/** Constructs an Enemy with the specified parameters. */
	public Enemy(EntityId id, EnemyPath path, int hp, double speed, int bounty) {
		this.id = id;
		this.path = path;
		this.position = path.startPosition();
		this.health = new Health(hp);
		this.speed = speed;
		this.bounty = bounty;
	}

	/** Getters for the Enemy properties */
	public EntityId id() {
		return this.id;
	}
	/** Get the current position of the enemy. */
	public Position position() {
		return this.position;
	}

	/** Get the health of the enemy. */
	public Health health() {
		return this.health;
	}

	/** Get the speed of the enemy. */
	public double speed() {
		return this.speed;
	}
	
	/** Get the bounty awarded for defeating the enemy. */
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
	 * @param next La position cible à atteindre
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

	/** Vérifie si l'ennemi a atteint la fin de son chemin. */
	public boolean isAtEnd() {
		return this.waypointIndex >= this.path.size();
	}

	@Override
	/** Provides a string representation of the Enemy object. */
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
