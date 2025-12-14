package com.towerdefense.domain.projectile;

import com.towerdefense.domain.*;

public class Projectile implements GameObject {
    private final EntityId id;
    private Position position;
    private final double speed;
    private final int damage;
    private final EntityId targetId;

    public Projectile(EntityId id, Position pos, double speed, int damage, EntityId targetId) {
        this.id = id;
        this.position = pos;
        this.speed = speed;
        this.damage = damage;
        this.targetId = targetId;
    }

    public EntityId id() { return id; }
    public Position position() { return position; }
    public int damage() { return damage; }
    public EntityId targetId() { return targetId; }

    public void updateTowards(Position target) {
        double dx = target.x() - position.x();
        double dy = target.y() - position.y();
        double dist = Math.sqrt(dx*dx + dy*dy);
        if (dist == 0) return;
        double normX = dx / dist;
        double normY = dy / dist;

        double move = speed;
        if (move >= dist) {
            position = target;
        } else {
            position = new Position(
                (int)(position.x() + normX * move),
                (int)(position.y() + normY * move)
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
    	builder.append(", speed:");
    	builder.append(speed);
    	builder.append("}");
    	return builder.toString();
    }
}
