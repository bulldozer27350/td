package com.towerdefense.domain.projectile;

import com.towerdefense.domain.EntityId;
import com.towerdefense.domain.GameObject;
import com.towerdefense.domain.GameTime;
import com.towerdefense.domain.Position;

/**
 * Represents a projectile in the tower defense game.
 */
public class Projectile implements GameObject {
	private final EntityId id;
	private Position position;
    private final double speedPerTick;
	private final int damage;
	private final EntityId targetId;

	/** Constructor to initialize projectile with given attributes. */
	public Projectile(EntityId id, Position pos, double speedCasesPerSecond, int damage, EntityId targetId) {
		this.id = id;
		this.position = pos;
        this.speedPerTick = GameTime.casesPerSecondToCasesPerTick(speedCasesPerSecond);
		this.damage = damage;
		this.targetId = targetId;
	}

	/** Getters for projectile attributes */
	public EntityId id() {
		return id;
	}

	/** Getters for projectile attributes */
	public Position position() {
		return position;
	}

	/** Getters for projectile attributes */
	public int damage() {
		return damage;
	}

	/** Getters for projectile attributes */
	public EntityId targetId() {
		return targetId;
	}

	public void updateTowards(Position target) {
        double dx = target.x() - position.x();
        double dy = target.y() - position.y();
        double dist = Math.sqrt(dx * dx + dy * dy);
        if (dist == 0) return;
        
        double normX = dx / dist;
        double normY = dy / dist;

        double move = speedPerTick;
        
        if (move >= dist) {
            position = target;
        } else {
            position = new Position(
                (int) (position.x() + normX * move),
                (int) (position.y() + normY * move)
            );
        }
    }

	@Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getName());
        builder.append(": {id:");
        builder.append(id);
        builder.append(", position:");
        builder.append(position);
        builder.append(", damage:");
        builder.append(damage);
        builder.append(", targetId:");
        builder.append(targetId);
        builder.append(", speedPerTick:");
        builder.append(speedPerTick);
        builder.append("}");
        return builder.toString();
    }
}
