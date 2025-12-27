package com.towerdefense.domain.dynamik.tower;

import com.towerdefense.domain.EntityId;
import com.towerdefense.domain.GameObject;
import com.towerdefense.domain.Position;
import com.towerdefense.domain.statik.tower.TowerLevelDefinition;
import com.towerdefense.domain.statik.tower.TowerType;

/**
 * Représente une tour dans le jeu. Une tour possède : - un identifiant unique -
 * une position dans le monde - une portée de tir - des dégâts infligés par
 * projectile - un temps de rechargement (cooldown)
 *
 * La tour ne "tire" pas elle-même : elle expose uniquement la logique
 * permettant de dire si elle est prête, si une cible est à portée, et de
 * déclencher son cooldown après un tir.
 */
public class Tower implements GameObject {

	/** Identifiant unique de la tour */
	private final EntityId id;

	/** Position actuelle de la tour */
	private Position position;

	/** Cooldown restant avant que la tour puisse tirer à nouveau */
	private double cooldownRemaining = 0.0;

	private final TowerType type;
	private int level = 1;
	private int upgradeRemainingTicks = 0;

	/** Constructeur */
	public Tower(EntityId id, Position pos, TowerType type) {
		this.id = id;
		this.position = pos;
		this.type = type;
	}

	/** Accesseurs */
	public int level() {
		return level;
	}

	/** Renvoie les statistiques actuelles de la tour en fonction de son niveau */
	public TowerLevelDefinition currentStats() {
		return type.level(level);
	}

	/** Améliore la tour d'un niveau */
	public void upgrade() {
		if (level >= type.maxLevel()) {
			throw new IllegalStateException("Max level reached");
		}
		level++;
	}

	/**
	 * Renvoie l'identifiant unique de la tour.
	 * 
	 * @return l'identifiant unique de la tour
	 */
	public EntityId id() {
		return id;
	}

	/**
	 * Renvoie la position actuelle de la tour.
	 */
	public Position position() {
		return position;
	}

	/**
	 * Renvoie la portée de tir actuelle de la tour.
	 */
	public double range() {
		return currentStats().range();
	}

	/**
	 * Renvoie les dégâts infligés par la tour.
	 */
	public int damage() {
		return currentStats().damage();
	}

	/**
	 * Renvoie le temps de rechargement actuel de la tour.
	 */
	public double reloadSeconds() {
		return currentStats().reloadSeconds();
	}

	/**
	 * Indique si la tour peut être améliorée.
	 */
	public boolean canUpgrade() {
		return upgradeRemainingTicks == 0 && level < type.maxLevel();
	}

	/**
	 * Renvoie la définition du niveau suivant de la tour.
	 */
	public TowerLevelDefinition nextLevelDefinition() {
		return type.level(level + 1);
	}

	/**
	 * Démarre l'amélioration de la tour vers le niveau suivant.
	 */
	public void startUpgrade(TowerLevelDefinition next) {
		this.upgradeRemainingTicks = next.buildTimeTicks();
	}

	/**
	 * Indique si la tour est prête à tirer. Une tour est prête lorsque son cooldown
	 * est écoulé.
	 */
	public boolean isReady() {
		return cooldownRemaining <= 0;
	}

	/**
	 * Indique si la tour est en cours d'amélioration.
	 */
	public boolean isUnderBuilding() {
		return this.upgradeRemainingTicks > 0;
	}

	/**
	 * Appelé à chaque tick : réduit le cooldown si nécessaire. Lorsqu'il atteint
	 * zéro, la tour peut tirer.
	 */
	public void tick() {
		if (upgradeRemainingTicks > 0) {
			upgradeRemainingTicks--;
			if (upgradeRemainingTicks == 0) {
				upgrade();
			}
			return;
		}
		if (cooldownRemaining > 0) {
			cooldownRemaining -= 1;
		}
	}

	/**
	 * Indique si une position cible donnée est dans la portée de la tour.
	 */
	public boolean canShootTarget(Position target) {
		return position.distanceTo(target) <= currentStats().range();
	}

	/**
	 * Donne les dégâts infligés par un projectile tiré par cette tour.
	 */
	public int computeShotDamage() {
		return currentStats().damage();
	}

	/**
	 * Déclenche un tir : remet le cooldown au temps de rechargement.
	 */
	public void triggerShot() {
		cooldownRemaining = currentStats().reloadSeconds();
	}

	@Override
	/**
	 * Renvoie une représentation textuelle de la tour
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getName());
		builder.append(": {id:");
		builder.append(id);
		builder.append(", position:");
		builder.append(position);
		builder.append(", range:");
		builder.append(currentStats().range());
		builder.append(", reloadSeconds:");
		builder.append(currentStats().reloadSeconds());
		builder.append(", ready:");
		builder.append(isReady());
		builder.append("}");
		return builder.toString();
	}
}