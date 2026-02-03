package com.towerdefense.domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.towerdefense.domain.dynamik.enemy.Enemy;
import com.towerdefense.domain.dynamik.level.LevelProgress;
import com.towerdefense.domain.dynamik.tower.Tower;
import com.towerdefense.domain.map.EnemyPath;
import com.towerdefense.domain.player.PlayerState;
import com.towerdefense.domain.projectile.Projectile;

/**
 * Represents the overall state of the game.
 * 
 * Utilise des Maps pour les lookups O(1)
 */
public class GameState {

    private final Map<EntityId, Tower> towersMap = new HashMap<>();
    private final Map<EntityId, Enemy> enemiesMap = new HashMap<>();
    private final Map<EntityId, Projectile> projectilesMap = new HashMap<>();
    
    private LevelProgress levelProgress;
    private StateEnum state;
    private PlayerState player;
    
    private Map<String, Integer> towerMaxRanks = new HashMap<>();
    private Map<String, EnemyPath> enemyPaths = new HashMap<>();

    public void setTowerMaxRanks(Map<String, Integer> maxRanks) {
        this.towerMaxRanks = maxRanks;
    }

    public int getTowerMaxRank(String towerTypeId) {
        return towerMaxRanks.getOrDefault(towerTypeId, 999); // Par défaut : tous les rangs
    }

    public Collection<Tower> towers() {
        return towersMap.values();
    }

    public Collection<Enemy> enemies() {
        return enemiesMap.values();
    }

    public Collection<Projectile> projectiles() {
        return projectilesMap.values();
    }

    /**
     * Récupère une tour par son ID en O(1).
     * 
     * @param id l'ID de la tour
     * @return la tour, ou null si non trouvée
     */
    public Tower getTower(EntityId id) {
        return towersMap.get(id);
    }
    
    /**
     * Récupère un ennemi par son ID en O(1).
     * 
     * @param id l'ID de l'ennemi
     * @return l'ennemi, ou null si non trouvé
     */
    public Enemy getEnemy(EntityId id) {
        return enemiesMap.get(id);
    }
    
    /**
     * Récupère un projectile par son ID en O(1).
     * 
     * @param id l'ID du projectile
     * @return le projectile, ou null si non trouvé
     */
    public Projectile getProjectile(EntityId id) {
        return projectilesMap.get(id);
    }
    
    public void addTower(Tower t) {
        towersMap.put(t.id(), t);
    }

    public void removeTower(EntityId id) {
        towersMap.remove(id);
    }

    public void addEnemy(Enemy e) {
        enemiesMap.put(e.id(), e);
    }

    public void removeEnemy(EntityId id) {
        enemiesMap.remove(id);
    }

    public void addProjectile(Projectile p) {
        projectilesMap.put(p.id(), p);
    }

    public void removeProjectile(EntityId id) {
        projectilesMap.remove(id);
    }

    public void setState(StateEnum state) {
        this.state = state;
    }

    public StateEnum getState() {
        return state;
    }

    public LevelProgress levelProgress() {
        if (levelProgress == null) {
            throw new IllegalStateException(
                "LevelProgress has not been initialized. " +
                "Call setLevelProgress() before accessing the game state."
            );
        }
        return levelProgress;
    }

    public void setLevelProgress(LevelProgress progress) {
        if (progress == null) {
            throw new IllegalArgumentException("LevelProgress cannot be null");
        }
        this.levelProgress = progress;
    }

    public PlayerState player() {
        return player;
    }

    public void setPlayer(PlayerState playerState) {
        this.player = playerState;
    }

    public int gridWidth() {
        return 15;
    }

    public int gridHeight() {
        return 15;
    }

    public GameObject objectAt(Position pos) {
        // Towers (position exacte)
        for (Tower t : towersMap.values()) {
            if (t.position().equals(pos)) {
                return t;
            }
        }

        // Enemies (position arrondie)
        for (Enemy e : enemiesMap.values()) {
            int ex = (int) Math.round(e.position().x());
            int ey = (int) Math.round(e.position().y());
            if (pos.x() == ex && pos.y() == ey) {
                return e;
            }
        }
        
        for (EnemyPath path : enemyPaths.values()) {
            for (int i = 0; i < path.size(); i++) {
                Position waypoint = path.waypoint(i);
                if (pos.equals(waypoint)) {
                    return pos;
                }
            }
        }

        // Projectiles (position arrondie)
        for (Projectile p : projectilesMap.values()) {
            int px = (int) Math.round(p.position().x());
            int py = (int) Math.round(p.position().y());
            if (pos.x() == px && pos.y() == py) {
                return p;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getName());
        builder.append(": {towers: {");
        builder.append("\n");
        this.towersMap.values().stream().forEach(t -> builder.append(t).append(", "));
        builder.append("\n");
        builder.append("}, enemies: {");
        builder.append("\n");
        this.enemiesMap.values().stream().forEach(e -> builder.append(e).append(", "));
        builder.append("\n");
        builder.append("}, projectiles:{");
        builder.append("\n");
        this.projectilesMap.values().stream().forEach(p -> builder.append(p).append(", "));
        builder.append("\n");
        builder.append("}");
        return builder.toString();
    }

    public void setEnemyPaths(Map<String, EnemyPath> enemyPaths) {
        this.enemyPaths = enemyPaths;
    }
}