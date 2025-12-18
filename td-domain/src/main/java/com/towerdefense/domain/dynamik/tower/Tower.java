package com.towerdefense.domain.dynamik.tower;

import com.towerdefense.domain.EntityId;
import com.towerdefense.domain.GameObject;
import com.towerdefense.domain.Position;
import com.towerdefense.domain.statik.tower.TowerLevelDefinition;
import com.towerdefense.domain.statik.tower.TowerType;

/**
 * Représente une tour dans le jeu.
 * Une tour possède :
 *  - un identifiant unique
 *  - une position dans le monde
 *  - une portée de tir
 *  - des dégâts infligés par projectile
 *  - un temps de rechargement (cooldown)
 *
 * La tour ne "tire" pas elle-même : elle expose uniquement la logique
 * permettant de dire si elle est prête, si une cible est à portée,
 * et de déclencher son cooldown après un tir.
 */
public class Tower implements GameObject {

    /** Identifiant unique de la tour */
    private final EntityId id;

    /** Position actuelle de la tour */
    private Position position;

    /** Portée maximale du tir */
    private final double range;

    /** Dégâts infligés par un projectile créé par cette tour */
    private final int damage;

    /** Durée du cooldown entre deux tirs (en secondes/ticks) */
    private final double reloadSeconds;

    /** Cooldown restant avant que la tour puisse tirer à nouveau */
    private double cooldownRemaining = 0.0;
    
    private final TowerType type;
    private int level = 1;

    public Tower(EntityId id, Position pos, TowerType type) {
        this.id = id;
        this.position = pos;
        this.type = type;
        this.range = currentStats().range();
        this.damage = currentStats().damage();
        this.reloadSeconds = currentStats().reloadSeconds();
    }

    public int level() {
        return level;
    }

    public TowerLevelDefinition currentStats() {
        return type.level(level);
    }

    public void upgrade() {
        if (level >= type.maxLevel()) {
            throw new IllegalStateException("Max level reached");
        }
        level++;
    }
    
    public EntityId id() { return id; }
    public Position position() { return position; }
    public double range() { return range; }
    public int damage() { return damage; }
    public double reloadSeconds() { return reloadSeconds; }

    /**
     * Indique si la tour est prête à tirer.
     * Une tour est prête lorsque son cooldown est écoulé.
     */
    public boolean isReady() {
        return cooldownRemaining <= 0;
    }

    /**
     * Appelé à chaque tick : réduit le cooldown si nécessaire.
     * Lorsqu'il atteint zéro, la tour peut tirer.
     */
    public void tick() {
        if (cooldownRemaining > 0) {
            cooldownRemaining -= 1;
        }
    }

    /**
     * Indique si une position cible donnée est dans la portée de la tour.
     */
    public boolean canShootTarget(Position target) {
        return position.distanceTo(target) <= range;
    }

    /** 
     * Donne les dégâts infligés par un projectile tiré par cette tour.
     */
    public int computeShotDamage() {
        return damage;
    }

    /**
     * Déclenche un tir : remet le cooldown au temps de rechargement.
     */
    public void triggerShot() {
        cooldownRemaining = reloadSeconds;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getName());
        builder.append(": {id:");
        builder.append(id);
        builder.append(", position:");
        builder.append(position);
        builder.append(", range:");
        builder.append(range);
        builder.append(", reloadSeconds:");
        builder.append(reloadSeconds);
        builder.append(", ready:");
        builder.append(isReady());
        builder.append("}");
        return builder.toString();
    }
}